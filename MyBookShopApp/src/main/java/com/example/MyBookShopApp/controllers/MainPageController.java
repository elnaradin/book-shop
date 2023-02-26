package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.dto.SearchWordDto;
import com.example.MyBookShopApp.model.book.BookEntity;
import com.example.MyBookShopApp.model.book.tags.TagEntity;
import com.example.MyBookShopApp.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class MainPageController {

    private final BookService bookService;

    @Autowired
    public MainPageController(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute("tagCloud")
    public List<TagEntity> tagCloud(){
        return bookService.getTagsList();
    }

    @ModelAttribute("recommendedBooks")
    public List<BookEntity> recommendedBooks() {
        return bookService.getPageOfRecommendedBooks(0, 6).getContent();
    }
    @ModelAttribute("recentBooks")
    public List<BookEntity> recentBooks() {
        return bookService.getPageOfRecommendedBooks(0, 6).getContent();
    }
    @ModelAttribute("popularBooks")
    public List<BookEntity> popularBooks() {
        return bookService.getPageOfRecommendedBooks(0, 6).getContent();
    }


    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto(){
        return new SearchWordDto();
    }

    @ModelAttribute("maxTagValue")
    public Integer maxTagVal(){
        return   bookService.getMaxTagValue();
    }
    @ModelAttribute("minTagValue")
    public Integer minTagVal(){
        return   bookService.getMinTagValue();
    }
    @GetMapping("/")
    public String mainPage() {
        return "index";
    }




}
