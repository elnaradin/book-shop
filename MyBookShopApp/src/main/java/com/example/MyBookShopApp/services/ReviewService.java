package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.dto.ResultDto;
import com.example.MyBookShopApp.dto.review.MyReviewDto;
import com.example.MyBookShopApp.dto.review.ReviewRatingDto;
import com.example.MyBookShopApp.errs.UserNotAuthenticatedException;
import com.example.MyBookShopApp.model.book.BookEntity;
import com.example.MyBookShopApp.model.book.review.BookReviewEntity;
import com.example.MyBookShopApp.model.book.review.BookReviewLikeEntity;
import com.example.MyBookShopApp.model.user.UserEntity;
import com.example.MyBookShopApp.repositories.BookRepository;
import com.example.MyBookShopApp.repositories.ReviewLikeRepository;
import com.example.MyBookShopApp.repositories.ReviewRepository;
import com.example.MyBookShopApp.services.security.BookstoreUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final UserService userService;
    private final ReviewLikeRepository likeRepository;
    @Value(value = "${min-review-length}")
    private int minReviewLength;


    public ResultDto saveBookReview(MyReviewDto reviewDto) throws NullPointerException, UserNotAuthenticatedException {
        ResultDto resultDto = new ResultDto();
        if (reviewDto.getText().length() < minReviewLength) {
            resultDto.setError("Отзыв слишком короткий. Напишите, пожалуйста, более развёрнутый отзыв");
            return resultDto;
        }
        BookReviewEntity reviewEntity = new BookReviewEntity();
        BookEntity bookEntity = bookRepository.getOne(reviewDto.getBookId());
        reviewEntity.setBook(bookEntity);
        UserEntity userEntity = userService.getCurUser();
        reviewEntity.setUser(userEntity);
        reviewEntity.setText(reviewDto.getText());
        reviewEntity.setTime(LocalDateTime.now());
        reviewRepository.save(reviewEntity);
        resultDto.setResult(true);
        return resultDto;
    }

    public ResultDto saveBookReviewRating(ReviewRatingDto ratingDto)
            throws NullPointerException {
        ResultDto resultDto = new ResultDto();
        BookReviewLikeEntity reviewLikeEntity = new BookReviewLikeEntity();

        UserEntity user = ((BookstoreUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getBookstoreUser();

        Optional<BookReviewLikeEntity> bookReviewOpt =
                likeRepository
                        .findBookReviewLikeEntityByReviewIdAndUserId(ratingDto.getReviewid(), user.getId());
        bookReviewOpt.ifPresent(likeRepository::delete);
        Optional<BookReviewEntity> optReview = reviewRepository.findById(ratingDto.getReviewid());
        if (optReview.isEmpty()) {
            resultDto.setError("review was not found");
            resultDto.setResult(false);
            return resultDto;
        }
        reviewLikeEntity.setReview(optReview.get());
        reviewLikeEntity.setValue(ratingDto.getValue());
        reviewLikeEntity.setTime(LocalDateTime.now());
        reviewLikeEntity.setUser(user);
        likeRepository.save(reviewLikeEntity);

        resultDto.setResult(true);
        return resultDto;
    }

}
