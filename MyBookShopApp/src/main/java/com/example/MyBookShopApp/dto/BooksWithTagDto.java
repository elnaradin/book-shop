package com.example.MyBookShopApp.dto;

import com.example.MyBookShopApp.model.book.tags.TagEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BooksWithTagDto {
    private TagEntity tag;
    private List<BookDto> books;
}
