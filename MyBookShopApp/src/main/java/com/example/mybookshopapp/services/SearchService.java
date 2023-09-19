package com.example.mybookshopapp.services;

import com.example.mybookshopapp.dto.book.BooksPageDto;
import com.example.mybookshopapp.dto.book.request.RequestDto;

public interface SearchService {
    BooksPageDto getPageOfSearchResultBooks(RequestDto request);

    BooksPageDto getPageOfGoogleBooksApiSearchResult(RequestDto request);
}
