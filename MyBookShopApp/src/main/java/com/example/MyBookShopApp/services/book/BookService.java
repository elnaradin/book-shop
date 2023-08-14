package com.example.MyBookShopApp.services.book;

import com.example.MyBookShopApp.dto.book.BooksPageDto;
import com.example.MyBookShopApp.dto.book.FullBookDto;
import com.example.MyBookShopApp.dto.request.RequestDto;

public interface BookService {
    FullBookDto getFullBookInfoBySlug(String bookSlug);

    BooksPageDto getRecommendedBooksPage(RequestDto requestDto);

    BooksPageDto getRecentBooksPage(RequestDto requestDto);

    BooksPageDto getPopularBooksPage(RequestDto request);

    BooksPageDto getPageOfSearchResultBooks(RequestDto request);

}
