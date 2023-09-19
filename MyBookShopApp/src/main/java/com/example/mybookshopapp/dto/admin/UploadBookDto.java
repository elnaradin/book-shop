package com.example.mybookshopapp.dto.admin;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
@Data
public class UploadBookDto {
    private String oldSlug;
    private String slug;

    private String title;

    private String description;
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate pubDate;
    private Integer price;

    private Integer discount;
    private int isBestseller;
    private List<String> authors;
    private List<String> genres;
    private List<String> tags;
    private MultipartFile image;

    private MultipartFile pdf;
    private MultipartFile epub;
    private MultipartFile fb2;

}
