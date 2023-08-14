package com.example.MyBookShopApp.services.ratingAndReview;

import com.example.MyBookShopApp.dto.ResultDto;
import com.example.MyBookShopApp.dto.book.RatingDto;
import com.example.MyBookShopApp.dto.review.BookRatingDto;
import com.example.MyBookShopApp.dto.review.MyReviewDto;
import com.example.MyBookShopApp.dto.review.ReviewDto;
import com.example.MyBookShopApp.dto.review.ReviewLikeDto;
import com.example.MyBookShopApp.model.book.BookEntity;
import com.example.MyBookShopApp.model.book.RatingEntity;
import com.example.MyBookShopApp.model.book.review.BookReviewEntity;
import com.example.MyBookShopApp.model.book.review.BookReviewLikeEntity;
import com.example.MyBookShopApp.model.user.UserEntity;
import com.example.MyBookShopApp.repositories.BookRepository;
import com.example.MyBookShopApp.repositories.RatingRepository;
import com.example.MyBookShopApp.repositories.ReviewLikeRepository;
import com.example.MyBookShopApp.repositories.ReviewRepository;
import com.example.MyBookShopApp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RatingReviewServiceImpl implements RatingReviewService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public RatingDto getBookRating(String slug) {
        return bookRepository.getBookRatingsCountBySlug(slug);
    }
    @Override
    public List<ReviewDto> getBookReviews(String slug) {
        return reviewRepository.getReviewListBySlug(slug);
    }

    @Override
    public List<ReviewDto> getBookReviews(String slug, String email) {
        return reviewRepository.getReviewListBySlugAndEmail(slug, email);
    }

    @Override
    @Transactional
    public ResultDto addRating(BookRatingDto bookRatingDto, Authentication authentication) {
        UserEntity user = getUser(authentication.getName());
        BookEntity book = getBook(bookRatingDto.getSlug());
        if (!ratingRepository.existsByUserAndBook(user, book)) {
            RatingEntity rating = RatingEntity.builder()
                    .value(bookRatingDto.getValue())
                    .user(user)
                    .book(book)
                    .build();
            ratingRepository.saveAndFlush(rating);
        } else {
            ratingRepository.updateValueByUserAndBook(bookRatingDto.getValue(), user, book);
        }

        return ResultDto.builder().result(true).build();
    }

    @Override
    @Transactional
    public ResultDto addReviewRating(
            ReviewLikeDto reviewLikeDto,
            String userEmail
    ) {
        UserEntity user = getUser(userEmail);
        Optional<BookReviewEntity> review = reviewRepository.findById(reviewLikeDto.getReviewid());
        if (!reviewLikeRepository.existsByReviewAndUser(review.orElseThrow(), user)) {
            BookReviewLikeEntity reviewLike = BookReviewLikeEntity.builder()
                    .review(review.get())
                    .user(user)
                    .value(reviewLikeDto.getValue())
                    .time(LocalDateTime.now())
                    .build();
            reviewLikeRepository.saveAndFlush(reviewLike);
        } else {
            reviewLikeRepository.updateTimeAndValueByUserAndReview(
                    LocalDateTime.now(),
                    reviewLikeDto.getValue(),
                    user,
                    review.get()
            );
        }
        return ResultDto.builder().result(true).build();
    }
    @Override
    @Transactional
    public ResultDto addBookReview(
            MyReviewDto reviewDto,
            Authentication authentication
    ) {
        BookEntity book = getBook(reviewDto.getSlug());
        UserEntity user = getUser(authentication.getName());
        if (!reviewRepository.existsByBookAndUser(book, user)) {
            BookReviewEntity review = BookReviewEntity.builder()
                    .book(book)
                    .user(user)
                    .time(LocalDateTime.now())
                    .text(reviewDto.getText()).build();
            reviewRepository.saveAndFlush(review);
        } else {
            reviewRepository.updateTextAndTimeByBookAndUser(
                    reviewDto.getText(),
                    LocalDateTime.now(),
                    book,
                    user
            );
        }
        return ResultDto.builder()
                .result(true).build();
    }

    private UserEntity getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }
    private BookEntity getBook(String slug) {
        return bookRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("book not found"));
    }
}
