package com.example.MyBookShopApp.dto.author;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmallAuthorsDto {
    private String name;
    private String slug;
}
