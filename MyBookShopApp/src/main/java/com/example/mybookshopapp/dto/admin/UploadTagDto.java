package com.example.mybookshopapp.dto.admin;

import com.example.mybookshopapp.dto.tag.ShortTagDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UploadTagDto extends ShortTagDto {
    private String oldSlug;
}
