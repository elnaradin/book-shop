package com.example.MyBookShopApp.services.file;

import com.example.MyBookShopApp.config.security.IAuthenticationFacade;
import com.example.MyBookShopApp.model.book.file.BookFileEntity;
import com.example.MyBookShopApp.model.enums.StatusType;
import com.example.MyBookShopApp.repositories.Book2UserRepository;
import com.example.MyBookShopApp.repositories.BookFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final BookFileRepository bookFileRepository;
    private final IAuthenticationFacade facade;
    private final Book2UserRepository book2UserRepository;

    @Override
    public List<BookFileEntity> getFilesBySlug(String slug) {
        String status = book2UserRepository.getCodeByBookSlugAndEmail(slug, facade.getCurrentUsername());
        if(status.equals(StatusType.PAID.toString()) || status.equals(StatusType.ARCHIVED.toString())){
            return bookFileRepository.findFilesByBookSlug(slug);
        }
        return new ArrayList<>();
    }
}
