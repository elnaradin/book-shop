package com.example.mybookshopapp.dto.review;

import lombok.Data;

@Data
public class ReviewLikeDto {
    private String reviewid;
    private Short value;
    private String slug;

}
