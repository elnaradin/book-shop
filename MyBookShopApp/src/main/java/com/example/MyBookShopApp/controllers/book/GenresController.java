package com.example.MyBookShopApp.controllers.book;

import com.example.MyBookShopApp.dto.GenreDto;
import com.example.MyBookShopApp.dto.SearchWordDto;
import com.example.MyBookShopApp.services.BookService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@Controller
public class GenresController {
    @Value("${books-pool-limit.genre}")
    private Integer limit;
    @Value("${universal-initial-offset}")
    private Integer offset;
    private final BookService bookService;

    public GenresController(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute("genreList")
    public List<GenreDto> genreEntityList() {
        return bookService.getGenres();
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
