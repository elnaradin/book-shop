package com.example.mybookshopapp.services;

import com.example.mybookshopapp.dto.ResultDto;
import com.example.mybookshopapp.dto.author.AuthorDto;
import com.example.mybookshopapp.dto.author.FullAuthorDto;
import com.example.mybookshopapp.dto.author.ShortAuthorDto;
import com.example.mybookshopapp.dto.admin.UploadAuthorDto;
import com.example.mybookshopapp.dto.book.BooksPageDto;
import com.example.mybookshopapp.dto.book.request.RequestDto;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface AuthorService {
    FullAuthorDto getFullAuthorInfo(String slug);

    ShortAuthorDto getShortAuthorInfo(String slug);

    Map<String, List<ShortAuthorDto>> createAuthorsMap();

    BooksPageDto getBooksPageByAuthor(RequestDto request);

    List<ShortAuthorDto> getAuthorsListBySlug(String slug);

    List<ShortAuthorDto> getSortedAuthorList();

    ResultDto saveOrUpdateAuthor(UploadAuthorDto uploadAuthorDto) throws IOException;


    AuthorDto getAuthorUpdateData(String slug);

    ResultDto deleteAuthor(String slug);
}
