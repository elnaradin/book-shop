package com.example.MyBookShopApp.dto.bookCollections;

import com.example.MyBookShopApp.dto.book.BookDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BooksWithTotalPrice {
    private List<BookDto> books;

    public Integer getDiscountTotalPrice() {
        return books.stream().map(BookDto::getDiscountPrice).reduce(0, Integer::sum);
    }

    public List<Integer> getBookIds() {
        return books.stream().map(BookDto::getId).collect(Collectors.toList());
    }

    public Integer getOldTotalPrice() {
        return books.stream().map(BookDto::getPrice).reduce(0, Integer::sum);
    }
}
