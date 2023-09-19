package com.example.mybookshopapp.services;

import com.example.mybookshopapp.dto.file.BookFileDto;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
    List<BookFileDto> getFilesBySlug(String slug);

    List<BookFileDto> getFilesBySlugAdmin(String slug);

    void uploadAndSetBookCover(String slug, MultipartFile file) throws IOException;

    void uploadAndSetAuthorPhoto(String slug, MultipartFile file) throws IOException;

    ResponseEntity<ByteArrayResource> downloadBook( String slug, String hash) throws IOException;

    boolean isDownloadLimitReached(String slug);
}
