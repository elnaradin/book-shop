package com.example.MyBookShopApp.dto.author;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShortAuthorDto {
    private String slug;
    private String name;

}
