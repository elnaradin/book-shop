package com.example.MyBookShopApp.dto.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
    private SmallReviewDto review;

    private Integer rating;
    private Integer likeCount;
    private Integer dislikeCount;

}
