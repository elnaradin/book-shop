package com.example.mybookshopapp.dto.file;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookFileDto {
    private String hash;
    private String typeName;
    private String typeDescription;
    private String length;

    public BookFileDto(String hash, String typeName, String typeDescription) {
        this.hash = hash;
        this.typeName = typeName;
        this.typeDescription = typeDescription;
    }
}
