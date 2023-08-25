package com.example.MyBookShopApp.dto.book;

import lombok.Data;

@Data
public class ShortBookDto implements ShortBookDtoProjection {
    private String slug;
    private String image;
    private String title;
    private Integer discount;
    private Boolean isBestseller;
    private Integer price;
    private Integer discountPrice;
    private String authors;
}
