package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.model.book.BookEntity;
import com.example.MyBookShopApp.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class RecentBooksController {
    private final BookService bookService;

    @Autowired
    public RecentBooksController(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute("booksList")
    public List<BookEntity> bookList(){
        return bookService.getBooksData();
    }
    @GetMapping("/books/recent")
    public String recentBooksPage(){
        return "/books/recent";
    }
}
