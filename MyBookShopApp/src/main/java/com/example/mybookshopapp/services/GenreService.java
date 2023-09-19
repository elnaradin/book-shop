package com.example.mybookshopapp.services;

import com.example.mybookshopapp.dto.ResultDto;
import com.example.mybookshopapp.dto.book.BooksPageDto;
import com.example.mybookshopapp.dto.book.request.RequestDto;
import com.example.mybookshopapp.dto.genre.GenreDto;
import com.example.mybookshopapp.dto.genre.ShortGenreDto;
import com.example.mybookshopapp.dto.admin.UploadGenreDto;

import java.util.List;

public interface GenreService {
    ShortGenreDto getShortGenreInfo(String genreSlug);

    GenreDto getGenreTree();

    BooksPageDto getBooksPageByGenre(RequestDto request);

    List<ShortGenreDto> getSortedGenreList();

    ResultDto saveOrUpdateGenre(UploadGenreDto uploadGenreDto);

    List<ShortGenreDto> getShortGenresListBySlug(String slug);

    ResultDto deleteGenre(String slug);
}
