package com.example.MyBookShopApp.controllers.book;

import com.example.MyBookShopApp.dto.book.BooksPageDto;
import com.example.MyBookShopApp.dto.tag.TagDto;
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


    @ModelAttribute("recommendedBooks")
    public BooksPageDto recommendedBooks() {
        return bookService.getPageOfRecommendedBooks(offset, limit);
    }

    @ModelAttribute("recentBooks")
    public BooksPageDto recentBooks()  {
        return bookService.getPageOfRecentBooksFromMonthAgo(offset, limit);
    }

    @ModelAttribute("popularBooks")
    public BooksPageDto popularBooks() {
        return ratingAndPopularityService.getListOfPopularBooks(offset, limit);
    }
    // TODO: 26.04.2023 optimize tag size calculation

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
