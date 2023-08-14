package com.example.MyBookShopApp.dto.author;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class FullAuthorDto extends ShortAuthorDto {
    private String photo;
    private String description;

    public FullAuthorDto(String slug, String name, String photo, String description) {
        super(slug, name);
        this.photo = photo;
        this.description = description;
    }
}
