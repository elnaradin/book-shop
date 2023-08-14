package com.example.MyBookShopApp.dto.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookRatingDto {
    @JsonProperty("bookId")
    private String slug;
    private Integer value;
}
