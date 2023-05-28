package com.example.MyBookShopApp.dto.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BooksWithTotalPrice {
    private List<SlugBookDto> books = new ArrayList<>();


    public Integer getDiscountTotalPrice() {
        return books.stream().map(SlugBookDto::getDiscountPrice).reduce(0, Integer::sum);
    }

    public List<Integer> getBookIds() {
        return books.stream().map(SlugBookDto::getId).collect(Collectors.toList());
    }

    public Integer getOldTotalPrice() {
        return books.stream().map(SlugBookDto::getPrice).reduce(0, Integer::sum);
    }
}
