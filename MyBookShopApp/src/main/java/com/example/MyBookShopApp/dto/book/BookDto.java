package com.example.MyBookShopApp.dto.book;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data

public class BookDto extends AbstractBook {
    private String authors;
}
