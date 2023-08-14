package com.example.MyBookShopApp.services.ratingAndReview;

import com.example.MyBookShopApp.dto.ResultDto;
import com.example.MyBookShopApp.dto.book.RatingDto;
import com.example.MyBookShopApp.dto.review.BookRatingDto;
import com.example.MyBookShopApp.dto.review.MyReviewDto;
import com.example.MyBookShopApp.dto.review.ReviewDto;
import com.example.MyBookShopApp.dto.review.ReviewLikeDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface RatingReviewService {
    RatingDto getBookRating(String slug);

    List<ReviewDto> getBookReviews(String slug);

    List<ReviewDto> getBookReviews(String slug, String email);

    ResultDto addRating(BookRatingDto bookRatingDto, Authentication authentication);

    ResultDto addReviewRating(ReviewLikeDto reviewLikeDto, String authentication);

    ResultDto addBookReview(MyReviewDto reviewDto, Authentication authentication);
}
