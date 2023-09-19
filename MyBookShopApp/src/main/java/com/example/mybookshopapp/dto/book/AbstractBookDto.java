package com.example.mybookshopapp.dto.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractBookDto {
    private String slug;
    private String image;
    private String title;
    private int discount;
    @JsonProperty("isBestseller")
    private boolean isBestseller;
    private int price;
    private int discountPrice;
}
