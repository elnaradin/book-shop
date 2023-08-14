package com.example.MyBookShopApp.services.file;

import com.example.MyBookShopApp.model.book.file.BookFileEntity;

import java.util.List;

public interface FileService {
    List<BookFileEntity> getFilesBySlug(String slug);
}
