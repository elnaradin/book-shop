package com.example.MyBookShopApp.dto.book;

public interface FullBookDto {
    String getSlug();
    String getImage();
    String getTitle();
    Integer getDiscount();
    Boolean getIsBestseller();
    Integer getPrice();
    Integer getDiscountPrice();
    String getDescription();
    String getStatus();
    Integer getRating();
}
