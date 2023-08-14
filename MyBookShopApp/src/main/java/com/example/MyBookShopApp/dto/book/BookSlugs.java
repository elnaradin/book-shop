package com.example.MyBookShopApp.dto.book;

import com.example.MyBookShopApp.model.enums.StatusType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookSlugs {
    private StatusType status;
    private String slug;


}
