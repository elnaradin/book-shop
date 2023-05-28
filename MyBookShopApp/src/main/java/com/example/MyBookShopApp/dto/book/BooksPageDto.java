package com.example.MyBookShopApp.dto.book;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BooksPageDto {
    private Integer count;
    private List<BookDto> books;

    public BooksPageDto(Integer count, List<BookDto> books) {
        this.count = count;
        this.books = books;
    }
}
