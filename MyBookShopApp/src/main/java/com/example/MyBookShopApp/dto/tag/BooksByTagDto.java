package com.example.MyBookShopApp.dto.tag;

import com.example.MyBookShopApp.dto.book.BookDto;
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
public class BooksByTagDto {
    private Integer count;
    private TagEntity tag;
    private List<BookDto> books;
}
