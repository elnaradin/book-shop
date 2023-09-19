package com.example.mybookshopapp.controllers.book;

import com.example.mybookshopapp.config.security.IAuthenticationFacade;
import com.example.mybookshopapp.dto.book.request.RequestDto;
import com.example.mybookshopapp.services.author.AuthorService;
import com.example.mybookshopapp.services.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class AuthorsController {
    @Value("${books-batch-size.pool}")
    private Integer limit;
    private final AuthorService authorService;
    private final FileService fileService;
    private final IAuthenticationFacade facade;
    private static final String AUTHOR_ATTRIBUTE = "author";

    @GetMapping("/authors")
    public String allAuthorsPage(Model model) {
        model.addAttribute("authorsMap", authorService.createAuthorsMap());
        return "/authors/index";
    }
    @PostMapping("/authors/{slug}/img/save")
    public String saveNewAuthorPhoto(@PathVariable String slug, @RequestParam("file") MultipartFile file) throws IOException {
        fileService.uploadAndSetAuthorPhoto(slug, file);
        return "redirect:/authors/" + slug;
    }

    @GetMapping("/authors/{slug}")
    public String authorPage(@PathVariable("slug") String slug, Model model) {
        if(facade.isAdmin()){
            model.addAttribute(AUTHOR_ATTRIBUTE, authorService.getAuthorUpdateData(slug));
        } else {
            model.addAttribute(AUTHOR_ATTRIBUTE, authorService.getFullAuthorInfo(slug));
            model.addAttribute("booksPage", authorService.getBooksPageByAuthor(
                    RequestDto.builder().slug(slug).offset(0).limit(5).build()
            ));
        }
        return "/authors/slug";
    }

    @GetMapping("/books/author/{slug}")
    public String authorBooksPage(@PathVariable("slug") String slug, Model model) {
        model.addAttribute(AUTHOR_ATTRIBUTE, authorService.getShortAuthorInfo(slug));
        model.addAttribute("booksPage", authorService.getBooksPageByAuthor(
                RequestDto.builder().slug(slug).offset(0).limit(limit).build()
        ));
        return "/books/author";
    }
}
