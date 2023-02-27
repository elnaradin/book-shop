package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.dto.SearchWordDto;
import com.example.MyBookShopApp.model.genre.GenreEntity;
import com.example.MyBookShopApp.services.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@Controller
public class GenresController {
    private final BookService bookService;

    public GenresController(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto(){
        return new SearchWordDto();
    }

    @ModelAttribute("genreList")
    public List<GenreEntity> genreEntityList(){
        return bookService.getGenres();
    }

    @GetMapping("/genres")
    public String genresPage() {
        return "/genres/index";
    }

    @GetMapping("/genres/{slug}")
    public String genresPage(@PathVariable("slug") String slug, Model model) {
        model.addAttribute("booksByGenre", bookService.getBooksByGenre(slug, 0, 15));
        return "/genres/slug";
    }
}
