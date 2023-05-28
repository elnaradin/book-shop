package com.example.MyBookShopApp.controllers.genres;

import com.example.MyBookShopApp.model.genre.GenreEntity;
import com.example.MyBookShopApp.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;


@Controller
@RequiredArgsConstructor
public class GenresController {
    @Value("${books-pool-limit.genre}")
    private Integer limit;
    @Value("${universal-initial-offset}")
    private Integer offset;
    private final BookService bookService;


    @ModelAttribute("genreList")
    public GenreEntity genreEntityList() {
        return bookService.getGenreTree();
    }

    @ModelAttribute("bookCount")
    public Map<Integer, Long> bookCountPerGenreMap() {
        return bookService.getBookCountPerGenre();
    }

    @GetMapping("/genres")
    public String genresPage() {
        return "/genres/index";
    }

    @GetMapping("/genres/{slug}")
    public String genresPage(@PathVariable("slug") String slug, Model model) {
        model.addAttribute("booksByGenre", bookService.getBooksByGenre(slug, offset, limit));
        return "/genres/slug";
    }
}
