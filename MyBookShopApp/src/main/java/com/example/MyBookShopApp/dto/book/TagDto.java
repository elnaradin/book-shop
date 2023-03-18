package com.example.MyBookShopApp.dto.book;

import com.example.MyBookShopApp.model.book.tags.TagEntity;
import lombok.Data;

@Data
public class TagDto {
    private Integer id;
    private String name;
    private Integer booksAmount;

    public TagDto(TagEntity tag, Integer booksAmount) {
        id = tag.getId();
        name = tag.getName();
        this.booksAmount = booksAmount;
    }
}
