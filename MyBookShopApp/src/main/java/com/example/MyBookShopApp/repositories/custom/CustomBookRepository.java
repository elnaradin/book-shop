package com.example.MyBookShopApp.repositories.custom;

import com.example.MyBookShopApp.dto.review.ReviewDto;

import java.util.List;
import java.util.Map;

public interface CustomBookRepository {
    Map<String, Integer> countRatingsByStar(Integer bookId);

    List<ReviewDto> getReviewsById(Integer bookId);


}
