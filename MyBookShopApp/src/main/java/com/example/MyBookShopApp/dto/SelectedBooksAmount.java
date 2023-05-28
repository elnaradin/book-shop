package com.example.MyBookShopApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectedBooksAmount {
    private Integer cartBooksNum;
    private Integer postponedBooksNum;
    private Integer myBooksNum;
    private Integer archivedBooksNum;
}
