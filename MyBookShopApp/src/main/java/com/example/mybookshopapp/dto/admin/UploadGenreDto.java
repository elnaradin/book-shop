package com.example.mybookshopapp.dto.admin;

import com.example.mybookshopapp.dto.genre.ShortGenreDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UploadGenreDto extends ShortGenreDto {
    private String oldSlug;
    private String parentId;

}
