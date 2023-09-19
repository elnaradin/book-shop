package com.example.mybookshopapp.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadAuthorDto {
    private String oldSlug;
    private String slug;

    private String name;

    private String description;
    private MultipartFile photo;

}
