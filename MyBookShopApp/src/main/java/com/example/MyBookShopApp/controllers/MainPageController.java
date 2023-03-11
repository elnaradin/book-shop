package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.dto.BookDto;
import com.example.MyBookShopApp.dto.SearchWordDto;
import com.example.MyBookShopApp.dto.TagDto;
import com.example.MyBookShopApp.services.BookService;
import com.example.MyBookShopApp.services.BooksRatingAndPopularityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;


import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainPageController {
    @Value("${books-slider-limit}")
    private Integer limit;

    @Value("${universal-initial-offset}")
    private Integer offset;


    private final BooksRatingAndPopularityService ratingAndPopularityService;
    private final BookService bookService;


    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute("recommendedBooks")
    public List<BookDto> recommendedBooks() {
        return bookService.getPageOfRecommendedBooks(offset, limit);
    }

    @ModelAttribute("recentBooks")
    public List<BookDto> recentBooks() {
        return bookService.getPageOfRecentBooksFromMonthAgo(offset, limit);
    }

    @ModelAttribute("popularBooks")
    public List<BookDto> popularBooks() {
        return ratingAndPopularityService.getListOfPopularBooks(offset, limit);
    }

    @ModelAttribute("tagCloud")
    public List<TagDto> tagCloud() {
        return bookService.getTagsList();
    }

    @ModelAttribute("maxTagValue")
    public Integer maxTagVal() {
        return bookService.getMaxTagValue();
    }

    @ModelAttribute("minTagValue")
    public Integer minTagVal() {
        return bookService.getMinTagValue();
    }

    @GetMapping("/")
    public String mainPage() {
        return "index";
    }
}
