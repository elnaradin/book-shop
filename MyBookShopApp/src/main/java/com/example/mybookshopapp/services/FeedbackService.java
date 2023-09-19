package com.example.mybookshopapp.services;

import com.example.mybookshopapp.dto.ResultDto;
import com.example.mybookshopapp.dto.book.RatingDto;
import com.example.mybookshopapp.dto.review.BookRatingDto;
import com.example.mybookshopapp.dto.review.MyReviewDto;
import com.example.mybookshopapp.dto.review.ReviewDto;
import com.example.mybookshopapp.dto.review.ReviewLikeDto;

import java.util.List;

public interface FeedbackService {
    RatingDto getBookRating(String slug);

    List<ReviewDto> getBookReviews(String slug);

    ResultDto addRating(BookRatingDto bookRatingDto);

    ResultDto addReviewRating(ReviewLikeDto reviewLikeDto);

    ResultDto addBookReview(MyReviewDto reviewDto);

    Integer getBookRatingOfCurrentUser(String slug);

    ResultDto deleteReview(String hash);
}
