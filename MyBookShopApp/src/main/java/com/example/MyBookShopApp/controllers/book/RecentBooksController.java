package com.example.MyBookShopApp.controllers.book;

import com.example.MyBookShopApp.dto.book.BooksPageDto;
import com.example.MyBookShopApp.dto.book.DatesDto;
import com.example.MyBookShopApp.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Controller
@RequiredArgsConstructor
public class RecentBooksController {
    @Value("${books-pool-limit.recent}")
    private Integer limit;
    @Value("${universal-initial-offset}")
    private Integer offset;
    private final BookService bookService;


    @ModelAttribute("recentBooks")
    public BooksPageDto bookList() {
        return bookService.getPageOfRecentBooksFromMonthAgo(offset, limit);
    }

    @ModelAttribute("dates")
    public DatesDto getDefaultPeriodOfBookPublications() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        formatter = formatter.withLocale(Locale.ROOT);
        return new DatesDto(LocalDate.now().minusMonths(1).format(formatter),
                LocalDate.now().format(formatter));
    }


    @GetMapping("/books/recent")
    public String recentBooks() {
        return "/books/recent";
    }
}
