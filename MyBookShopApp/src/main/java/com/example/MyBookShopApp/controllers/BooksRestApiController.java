package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.dto.BookPageDto;
import com.example.MyBookShopApp.services.AuthorService;
import com.example.MyBookShopApp.services.BookService;
import io.swagger.annotations.Api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/api")
@Api(description = "book data api")
public class BooksRestApiController {
    private final BookService bookService;
    private final AuthorService authorService;

    public BooksRestApiController(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @GetMapping("/books/recommended")
    @ResponseBody
    public BookPageDto getRecommendedBookPage(@RequestParam("offset") Integer offset,
                                              @RequestParam("limit") Integer limit) {
        return new BookPageDto(bookService.getPageOfRecommendedBooks(offset, limit).getContent());
    }

    @GetMapping("/books/popular")
    @ResponseBody
    public BookPageDto getPopularBookPage(@RequestParam("offset") Integer offset,
                                         @RequestParam("limit") Integer limit) {
        return new BookPageDto(bookService.getListOfPopularBooks(offset, limit));
    }

    @GetMapping("/books/recent")
    @ResponseBody
    public BookPageDto getRecentBookPage(@RequestParam(value = "from", required = false) String from,
                                         @RequestParam(value = "to", required = false) String to,
                                         @RequestParam(value = "offset", required = false) Integer offset,
                                         @RequestParam(value = "limit", required = false) Integer limit) throws ParseException {
        return new BookPageDto(bookService.getPageOfRecentBooksByPubDate(from, to, offset, limit).getContent());
    }

    @GetMapping("/books/tag/{id}")
    public BookPageDto tagPage(@PathVariable("id") Integer tagId,
                          @RequestParam(value = "offset", required = false) Integer offset,
                          @RequestParam(value = "limit", required = false) Integer limit){
        return new BookPageDto(bookService.getTaggedBooks(tagId, offset, limit).getBooks());
    }

    @GetMapping("/books/genre/{slug}")
    public BookPageDto genrePage(@PathVariable("slug") String slug,
                               @RequestParam(value = "offset", required = false) Integer offset,
                               @RequestParam(value = "limit", required = false) Integer limit){
        return new BookPageDto(bookService.getBooksByGenre(slug, offset, limit).getBooks());
    }

    @GetMapping("/books/author/{slug}")
    public BookPageDto BookOfAuthorPage(@PathVariable("slug") String slug,
                                        @RequestParam(value = "offset", required = false) Integer offset,
                                        @RequestParam(value = "limit", required = false) Integer limit){
        return new BookPageDto(authorService.getAuthorWithBooks(slug, offset, limit).getBooks());
    }

}
