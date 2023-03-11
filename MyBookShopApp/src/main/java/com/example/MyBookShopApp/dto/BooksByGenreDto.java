package com.example.MyBookShopApp.dto;

import com.example.MyBookShopApp.model.genre.GenreEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BooksByGenreDto {
    private GenreEntity genre;
    private List<BookDto> books;

}
