package com.example.mybookshopapp.services.book;

import com.example.mybookshopapp.dto.ResultDto;
import com.example.mybookshopapp.dto.book.BookDto;
import com.example.mybookshopapp.dto.book.BooksPageDto;
import com.example.mybookshopapp.dto.book.FullBookDto;
import com.example.mybookshopapp.dto.admin.UploadBookDto;
import com.example.mybookshopapp.dto.book.request.RequestDto;

import java.io.IOException;

public interface BookService {
    FullBookDto getFullBookInfoBySlug(String bookSlug);

    BooksPageDto getRecommendedBooksPage(RequestDto requestDto);

    BooksPageDto getRecentBooksPage(RequestDto requestDto);

    BooksPageDto getPopularBooksPage(RequestDto request);


    ResultDto saveOrUpdateBook(UploadBookDto uploadBookDto) throws IOException;

    BookDto getBookUpdateData(String slug);

    ResultDto deleteBook(String slug);
}
