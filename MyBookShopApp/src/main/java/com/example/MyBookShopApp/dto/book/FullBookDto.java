package com.example.MyBookShopApp.dto.book;

import com.example.MyBookShopApp.dto.author.AuthorDto;
import com.example.MyBookShopApp.dto.tag.TagDto;
import lombok.Data;

import java.util.List;

@Data
public class FullBookDto {
    private List<TagDto> tags;
    private String description;
    private List<AuthorDto> authorsList;
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
