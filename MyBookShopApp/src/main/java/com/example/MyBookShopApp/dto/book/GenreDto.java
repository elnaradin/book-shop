package com.example.MyBookShopApp.dto.book;

import com.example.MyBookShopApp.model.genre.GenreEntity;
import lombok.Data;

import java.util.List;

@Data
public class GenreDto {
    private Integer id;
    private Integer parentId;
    private String slug;
    private String name;
    private Integer booksAmount;
    private List<GenreDto> children;

    public GenreDto(GenreEntity genreEntity, Integer booksAmount) {
        id = genreEntity.getId();
        parentId = genreEntity.getParentId();
        slug = genreEntity.getSlug();
        name = genreEntity.getName();
        this.booksAmount = booksAmount;
    }


}
