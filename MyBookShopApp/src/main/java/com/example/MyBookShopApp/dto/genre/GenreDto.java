package com.example.MyBookShopApp.dto.genre;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class GenreDto extends ShortGenreDto {
    private Integer id;

    private Integer parentId;
    private Long booksCount;
    private List<GenreDto> children = new ArrayList<>();

    public GenreDto(
            Integer id, String slug,
            String name, Integer parentId,
            Long booksCount
    ) {
        super(slug, name);
        this.id = id;
        this.parentId = parentId;
        this.booksCount = booksCount;
    }
}
