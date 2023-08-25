package com.example.MyBookShopApp.services.search;

import com.example.MyBookShopApp.dto.book.BooksPageDto;
import com.example.MyBookShopApp.dto.book.request.RequestDto;

public interface SearchService {
    BooksPageDto getPageOfSearchResultBooks(RequestDto request);

    BooksPageDto getPageOfGoogleBooksApiSearchResult(RequestDto request);
}
