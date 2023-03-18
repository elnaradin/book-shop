package com.example.MyBookShopApp.controllers.book;

import com.example.MyBookShopApp.dto.SearchWordDto;
import com.example.MyBookShopApp.dto.book.BookDto;
import com.example.MyBookShopApp.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class RecentBooksController {
    @Value("${books-pool-limit.recent}")
    private Integer limit;
    @Value("${universal-initial-offset}")
    private Integer offset;
    private final BookService bookService;

    @Autowired
    public RecentBooksController(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute("recentBooks")
    public List<BookDto> bookList() {
        return bookService.getPageOfRecentBooksFromMonthAgo(offset, limit);
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @GetMapping("/books/recent")
    public String recentBooks() {
        return "/books/recent";
    }
}
