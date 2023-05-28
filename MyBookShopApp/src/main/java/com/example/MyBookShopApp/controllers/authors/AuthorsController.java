package com.example.MyBookShopApp.controllers.authors;

import com.example.MyBookShopApp.errs.AuthorNotFoundException;
import com.example.MyBookShopApp.model.book.authors.AuthorEntity;
import com.example.MyBookShopApp.services.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AuthorsController {


    @Value("${books-pool-limit.author}")
    private Integer limit;
    @Value("${universal-initial-offset}")
    private Integer offset;

    private final AuthorService authorService;


    @ModelAttribute("authorsMap")
    public Map<String, List<AuthorEntity>> authorsMap() {
        return authorService.getAuthorsMap();
    }

    @GetMapping("/authors")
    public String allAuthorsPage() {
        return "/authors/index";
    }

    @GetMapping("/authors/{slug}")
    public String authorPage(@PathVariable("slug") String slug, Model model)
            throws NullPointerException, AuthorNotFoundException {
        model.addAttribute("authorWithSomeBooks", authorService.getAuthorWithBooks(slug, 0, 10));
        return "/authors/slug";
    }

    @GetMapping("/books/author/{slug}")
    public String authorBooksPage(@PathVariable("slug") String slug, Model model){
        model.addAttribute("authorWithBooks", authorService.getAuthorWithBooks(slug, offset, limit));
        return "/books/author";
    }
}
