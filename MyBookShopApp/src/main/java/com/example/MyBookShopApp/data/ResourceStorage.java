package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.model.book.file.BookFileEntity;
import com.example.MyBookShopApp.repositories.BookFileRepository;
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
import java.util.logging.Logger;

@Service
public class ResourceStorage {
    private final BookFileRepository bookFileRepository;
    @Value("${upload.path}")
    String uploadPath;

    @Value("${download.path}")
    String downloadPath;

    public ResourceStorage(BookFileRepository bookFileRepository) {
        this.bookFileRepository = bookFileRepository;
    }

    public String saveNewBookImage(String slug, MultipartFile file) throws IOException {
        String resourceURI = null;
        if (!file.isEmpty()) {
            if (!new File(uploadPath).exists()) {
                Files.createDirectories(Paths.get(uploadPath));
                Logger.getLogger(this.getClass().getSimpleName())
                        .info("created image folder in " + uploadPath);
            }
            String fileName = slug + "." + FilenameUtils.getExtension(file.getOriginalFilename());
            Path path = Paths.get(uploadPath, fileName);
            resourceURI = "/book-covers/" + fileName;
            file.transferTo(path);
            Logger.getLogger(this.getClass().getSimpleName()).info(fileName + "uploaded OK!");
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
