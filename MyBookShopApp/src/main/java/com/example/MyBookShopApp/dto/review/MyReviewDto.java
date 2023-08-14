package com.example.MyBookShopApp.dto.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class MyReviewDto {
    @JsonProperty("bookId")
    private String slug;
    @Size(min = 50, max = 500, message = "text length must be 50-500")
    private String text;
}
