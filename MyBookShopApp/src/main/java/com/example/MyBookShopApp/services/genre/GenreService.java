package com.example.MyBookShopApp.services.genre;

import com.example.MyBookShopApp.dto.book.BooksPageDto;
import com.example.MyBookShopApp.dto.book.request.RequestDto;
import com.example.MyBookShopApp.dto.genre.GenreDto;
import com.example.MyBookShopApp.dto.genre.ShortGenreDto;

public interface GenreService {
    ShortGenreDto getShortGenreInfo(String genreSlug);

    GenreDto getGenreTree();

    BooksPageDto getBooksPageByGenre(RequestDto request);
}
