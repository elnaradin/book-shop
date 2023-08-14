package com.example.MyBookShopApp.services.file;

import com.example.MyBookShopApp.model.book.file.BookFileEntity;
import com.example.MyBookShopApp.repositories.BookFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final BookFileRepository bookFileRepository;

    @Override
    public List<BookFileEntity> getFilesBySlug(String slug) {
        return bookFileRepository.findFilesByBookSlug(slug);
    }
}
