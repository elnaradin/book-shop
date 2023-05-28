package com.example.MyBookShopApp.dto.book;

import lombok.Data;

@Data
public abstract class AbstractBook {
    private int id;
    private String slug;
    private String image;
    private String title;
    private int discount;
    private Boolean isBestseller;
    private Integer rating;
    private String status;
    private int price;
    private int discountPrice;
}
