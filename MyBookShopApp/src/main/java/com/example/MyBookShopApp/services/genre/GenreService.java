package com.example.MyBookShopApp.services.genre;

import com.example.MyBookShopApp.dto.book.BooksPageDto;
import com.example.MyBookShopApp.dto.genre.GenreDto;
import com.example.MyBookShopApp.dto.genre.ShortGenreDto;
import com.example.MyBookShopApp.dto.request.RequestDto;
import org.springframework.security.core.Authentication;

public interface GenreService {
    ShortGenreDto getShortGenreInfo(String genreSlug);

    GenreDto getGenreTree();

    BooksPageDto getBooksPageByGenre(RequestDto request, Authentication authentication);
}
