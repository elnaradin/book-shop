package com.example.mybookshopapp.services.data;


import com.example.mybookshopapp.model.book.file.BookFileEntity;
import com.example.mybookshopapp.model.enums.FileType;
import com.example.mybookshopapp.repositories.BookFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
public class ResourceStorage {
    private final BookFileRepository bookFileRepository;
    @Value("${upload.path.book-covers}")
    private String uploadBookCoverPath;
    @Value("${upload.relative-path.book-covers}")
    private String uploadBookCoverRelativePath;
    @Value("${upload.path.author-photos}")
    private String uploadAuthorPhotoPath;
    @Value("${upload.relative-path.author-photos}")
    private String uploadAuthorPhotoRelativePath;
    @Value("${download.path.books}")
    private String uploadBookPath;

    @Value("${download.path.books}")
    private String downloadPath;

    public ResourceStorage(BookFileRepository bookFileRepository) {
        this.bookFileRepository = bookFileRepository;
    }

    public String saveNewFile(String slug, MultipartFile file, FileType type) throws IOException {
        String resourceURI = null;
        String uploadPath = null;
        String basePath = null;
        switch (type) {
            case BOOK_COVER -> {
                uploadPath = uploadBookCoverPath;
                basePath = uploadBookCoverRelativePath;
            }
            case AUTHOR_PHOTO -> {
                uploadPath = uploadAuthorPhotoPath;
                basePath = uploadAuthorPhotoRelativePath;
            }
            case BOOK -> {
                uploadPath = uploadBookPath;
                basePath = "/";
            }
        }

        if (file != null && !file.isEmpty()) {
            if (!new File(uploadPath).exists()) {
                Files.createDirectories(Paths.get(uploadPath));
                log.info("created image folder in " + uploadPath);
            }
            String fileName = slug + "." + FilenameUtils.getExtension(file.getOriginalFilename());
            Path path = Paths.get(uploadPath, fileName);
            resourceURI = basePath + fileName;

            Files.deleteIfExists(Paths.get(resourceURI));

            file.transferTo(path);
            log.info(fileName + "uploaded OK!");
        }

        return resourceURI;
    }

    public Path getBookFilePath(String hash) {
        BookFileEntity bookFile = bookFileRepository.findFirstByHash(hash);
        return Paths.get(bookFile.getPath());
    }

    public MediaType getBookFileMime(String hash) {
        BookFileEntity bookFile = bookFileRepository.findFirstByHash(hash);
        String mimeType = URLConnection.guessContentTypeFromName(
                Paths.get(bookFile.getPath()).getFileName().toString()
        );
        if (mimeType != null) {
            return MediaType.parseMediaType(mimeType);
        } else {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    public byte[] getBookFileByteArray(String hash) throws IOException {
        BookFileEntity bookFile = bookFileRepository.findFirstByHash(hash);
        Path path = Paths.get(downloadPath, bookFile.getPath());
        return Files.readAllBytes(path);
    }
}
