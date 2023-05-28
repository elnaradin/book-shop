package com.example.MyBookShopApp.dto.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookFileDto {
    private String hash;
    private String type;
}
