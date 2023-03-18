package com.example.MyBookShopApp.dto.bookCollections;

import com.example.MyBookShopApp.dto.book.BookDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BooksPageDto {
    private Integer count;
    private List<BookDto> books;

    public BooksPageDto(List<BookDto> books) {
        this.books = books;
    }
}
