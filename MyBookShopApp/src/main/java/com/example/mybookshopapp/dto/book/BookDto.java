package com.example.mybookshopapp.dto.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private String slug;

    private String title;

    private String description;
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate pubDate;
    private Integer price;

    private Integer discount;
    private int isBestseller;
    private String image;
}
