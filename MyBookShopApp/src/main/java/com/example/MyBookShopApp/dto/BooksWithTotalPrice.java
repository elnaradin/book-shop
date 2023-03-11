package com.example.MyBookShopApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BooksWithTotalPrice {
    private List<BookDto> books;

    public Integer getDiscountTotalPrice() {
        return books.stream().map(BookDto::getDiscountPrice).reduce(0, Integer::sum);
    }

    public Integer getOldTotalPrice() {
        return books.stream().map(BookDto::getPrice).reduce(0, Integer::sum);
    }
}
