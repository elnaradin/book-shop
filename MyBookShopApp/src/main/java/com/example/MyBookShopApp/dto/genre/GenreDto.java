package com.example.MyBookShopApp.dto.genre;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenreDto {
    private Integer genreId;
    private Long booksAmount;
}
