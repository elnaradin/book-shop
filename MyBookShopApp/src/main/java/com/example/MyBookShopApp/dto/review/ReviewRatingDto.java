package com.example.MyBookShopApp.dto.review;

import lombok.Data;

@Data
public class ReviewRatingDto {
    private int reviewid;
    private Short value;

    @Override
    public String toString() {
        return reviewid +
                "_" + value;
    }
}
