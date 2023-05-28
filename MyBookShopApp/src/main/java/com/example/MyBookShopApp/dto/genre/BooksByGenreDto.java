package com.example.MyBookShopApp.dto.genre;

import com.example.MyBookShopApp.dto.book.BookDto;
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
    private Integer count;
    private String genre;
    private String slug;
    private List<BookDto> books;

}
