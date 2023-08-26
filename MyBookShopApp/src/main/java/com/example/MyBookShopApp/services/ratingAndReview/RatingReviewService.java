package com.example.MyBookShopApp.services.ratingAndReview;

import com.example.MyBookShopApp.dto.ResultDto;
import com.example.MyBookShopApp.dto.book.RatingDto;
import com.example.MyBookShopApp.dto.review.BookRatingDto;
import com.example.MyBookShopApp.dto.review.MyReviewDto;
import com.example.MyBookShopApp.dto.review.ReviewDto;
import com.example.MyBookShopApp.dto.review.ReviewLikeDto;

import java.util.List;

public interface RatingReviewService {
    RatingDto getBookRating(String slug);

    List<ReviewDto> getBookReviews(String slug);

    ResultDto addRating(BookRatingDto bookRatingDto);

    ResultDto addReviewRating(ReviewLikeDto reviewLikeDto);

    ResultDto addBookReview(MyReviewDto reviewDto);

    Integer getBookRatingOfCurrentUser(String slug);
}
