package com.example.MyBookShopApp.controllers.book;

import com.example.MyBookShopApp.dto.SearchWordDto;
import com.example.MyBookShopApp.model.book.authors.AuthorEntity;
import com.example.MyBookShopApp.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@Controller
public class AuthorsController {
    @Value("${books-pool-limit.author}")
    private Integer limit;
    @Value("${universal-initial-offset}")
    private Integer offset;

    private final AuthorService authorService;

    @Autowired
    public AuthorsController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @ModelAttribute("authorsMap")
    public Map<String, List<AuthorEntity>> authorsMap() {
        return authorService.getAuthorsMap();
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @GetMapping("/authors")
    public String allAuthorsPage() {
        return "/authors/index";
    }

    @GetMapping("/authors/{slug}")
    public String authorPage(@PathVariable("slug") String slug, Model model) throws NullPointerException {
        model.addAttribute("authorWithSomeBooks", authorService.getAuthorWithBooks(slug, 0, 10));
        return "/authors/slug";
    }

    @GetMapping("/books/author/{slug}")
    public String authorBooksPage(@PathVariable("slug") String slug, Model model) throws NullPointerException {
        model.addAttribute("authorWithBooks", authorService.getAuthorWithBooks(slug, offset, limit));
        return "/books/author";
    }
}
