package com.example.MyBookShopApp.dto.book;

public interface ShortBookDto {
    String getSlug();

    String getImage();

    String getTitle();

    Integer getDiscount();

    Boolean getIsBestseller();

    Integer getPrice();

    Integer getDiscountPrice();

    String getAuthors();
}
