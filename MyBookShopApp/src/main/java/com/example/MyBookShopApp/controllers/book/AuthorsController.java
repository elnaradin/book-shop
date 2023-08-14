package com.example.MyBookShopApp.controllers.book;

import com.example.MyBookShopApp.dto.request.RequestDto;
import com.example.MyBookShopApp.services.author.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class AuthorsController {
    @Value("${books-batch-size.pool}")
    private Integer limit;
    private final AuthorService authorService;

    @GetMapping("/authors")
    public String allAuthorsPage(Model model) {
        model.addAttribute("authorsMap", authorService.createAuthorsMap());
        return "/authors/index";
    }

    @GetMapping("/authors/{slug}")
    public String authorPage(@PathVariable("slug") String slug, Model model) {
        model.addAttribute("author", authorService.getFullAuthorInfo(slug));
        model.addAttribute("booksPage", authorService.getBooksPageByAuthor(
                RequestDto.builder().slug(slug).offset(0).limit(5).build()
        ));
        return "/authors/slug";
    }

    @GetMapping("/books/author/{slug}")
    public String authorBooksPage(@PathVariable("slug") String slug, Model model) {
        model.addAttribute("author", authorService.getShortAuthorInfo(slug));
        model.addAttribute("booksPage", authorService.getBooksPageByAuthor(
                RequestDto.builder().slug(slug).offset(0).limit(limit).build()
        ));
        return "/books/author";
    }
}
