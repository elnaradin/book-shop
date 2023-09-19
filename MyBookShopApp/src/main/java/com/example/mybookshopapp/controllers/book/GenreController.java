package com.example.mybookshopapp.controllers.book;

import com.example.mybookshopapp.dto.book.request.RequestDto;
import com.example.mybookshopapp.services.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
@RequiredArgsConstructor
public class GenreController {
    @Value("${books-batch-size.pool}")
    private Integer limit;
    private final GenreService genreService;

    @GetMapping("/books/genres")
    public String genresPage(Model model) {
        model.addAttribute("genreTree", genreService.getGenreTree());
        return "/genres/index";
    }

    @GetMapping("/books/genres/{slug}")
    public String genresPage(@PathVariable("slug") String genreSlug, Model model) {
        model.addAttribute("genre", genreService.getShortGenreInfo(genreSlug));
        model.addAttribute("booksPage", genreService.getBooksPageByGenre(
                RequestDto.builder().slug(genreSlug).offset(0).limit(limit).build()));
        return "/genres/slug";
    }
}
