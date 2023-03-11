package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.dto.ReviewsDto;
import com.example.MyBookShopApp.model.book.BookEntity;
import com.example.MyBookShopApp.model.book.review.BookReviewEntity;
import com.example.MyBookShopApp.model.book.review.BookReviewLikeEntity;
import com.example.MyBookShopApp.model.user.UserEntity;
import com.example.MyBookShopApp.repositories.BookRepository;
import com.example.MyBookShopApp.repositories.ReviewLikeRepository;
import com.example.MyBookShopApp.repositories.ReviewRepository;
import com.example.MyBookShopApp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final ReviewLikeRepository likeRepository;

    public List<ReviewsDto> getBookReviews(String slug) {
        List<BookReviewEntity> reviewEntities = reviewRepository.findBookReviewEntitiesByBookSlug(slug);
        List<ReviewsDto> reviews = new ArrayList<>();
        for (BookReviewEntity reviewEntity : reviewEntities) {
            reviews.add(new ReviewsDto(reviewEntity,
                    likeRepository.countBookReviewLikeEntitiesByValueAndReviewId((short) 1, reviewEntity.getId()),
                    likeRepository.countBookReviewLikeEntitiesByValueAndReviewId((short) -1, reviewEntity.getId())));
        }
        reviews.sort(Comparator.reverseOrder());
        return reviews;
    }

    public void saveBookReview(Integer bookId, String text) throws NullPointerException {
        BookReviewEntity reviewEntity = new BookReviewEntity();
        BookEntity bookEntity = bookRepository.getOne(bookId);
        reviewEntity.setBook(bookEntity);
        UserEntity userEntity = userRepository.getOne(1); /** temporary user**/
        reviewEntity.setUser(userEntity);
        reviewEntity.setText(text);
        reviewEntity.setTime(LocalDateTime.now());
        reviewRepository.save(reviewEntity);
    }

    public void saveBookReviewRating(Integer reviewId, Short value) throws NullPointerException {
        BookReviewLikeEntity reviewLikeEntity = new BookReviewLikeEntity();
        Optional<BookReviewLikeEntity> bookReviewOpt = likeRepository.findBookReviewLikeEntityByReviewId(reviewId);
        bookReviewOpt.ifPresent(likeRepository::delete);
        BookReviewEntity bookReviewEntity = reviewRepository.getOne(reviewId);
        UserEntity userEntity = userRepository.getOne(1); /** temporary user**/
        reviewLikeEntity.setReview(bookReviewEntity);
        reviewLikeEntity.setValue(value);
        reviewLikeEntity.setTime(LocalDateTime.now());
        reviewLikeEntity.setUser(userEntity);
        likeRepository.save(reviewLikeEntity);
    }
}
