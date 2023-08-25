package com.example.MyBookShopApp.services.author;

import com.example.MyBookShopApp.dto.author.FullAuthorDto;
import com.example.MyBookShopApp.dto.author.ShortAuthorDto;
import com.example.MyBookShopApp.dto.book.BooksPageDto;
import com.example.MyBookShopApp.dto.book.request.RequestDto;

import java.util.List;
import java.util.Map;

public interface AuthorService {
    FullAuthorDto getFullAuthorInfo(String slug);

    ShortAuthorDto getShortAuthorInfo(String slug);

    Map<String, List<ShortAuthorDto>> createAuthorsMap();

    BooksPageDto getBooksPageByAuthor(RequestDto request);

    List<ShortAuthorDto> getAuthorsList(String slug);
}
