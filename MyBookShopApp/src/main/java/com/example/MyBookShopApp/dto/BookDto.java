package com.example.MyBookShopApp.dto;

import com.example.MyBookShopApp.model.book.BookEntity;
import com.example.MyBookShopApp.model.book.authors.AuthorEntity;
import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Data;

import java.util.List;

@Data
public class BookDto {
    private int id;
    private String slug;
    private String image;
    private String description;
    private List<AuthorEntity> authors;
    private String title;
    private int discount;
    private boolean isBestseller;
    private Integer rating;
    private String status;
    private int price;
    private int discountPrice;

    public BookDto(BookEntity bookEntity, List<AuthorEntity> authors, String status, Integer rating) {
        id = bookEntity.getId();
        slug = bookEntity.getSlug();
        image = bookEntity.getImage();
        description = bookEntity.getDescription();
        this.authors = authors;
        title = bookEntity.getTitle();
        discount = bookEntity.getDiscount();
        isBestseller = bookEntity.getIsBestseller() == 1;
        this.status = status;
        price = bookEntity.getPrice();
        discountPrice = getDiscountPrice();
        this.rating = rating;
    }

    public Integer getDiscountPrice() {
        return Math.toIntExact(Math.round(price * (1.0 - (double) discount / 100)));
    }

    @JsonGetter(value = "authorNames")
    public String getAuthorNames() {
        if(authors.isEmpty()){
            return "no author specified";
        }
        return authors.size() > 1
                ? authors.get(0).getName().concat(" и прочие")
                : authors.get(0).getName();
    }

}
