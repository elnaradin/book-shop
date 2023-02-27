package com.example.MyBookShopApp.dto;

import com.example.MyBookShopApp.model.book.BookEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class BookPageDto {
    private Integer count;
    private List<BookEntity> books;
    public BookPageDto(List<BookEntity> books) {
        this.books = books;
        count = books.size();
    }
}
