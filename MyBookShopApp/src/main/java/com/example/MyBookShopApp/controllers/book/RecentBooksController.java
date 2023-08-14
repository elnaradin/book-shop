package com.example.MyBookShopApp.controllers.book;

import com.example.MyBookShopApp.dto.request.RequestDto;
import com.example.MyBookShopApp.services.book.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class RecentBooksController {
    @Value("${books-batch-size.slider}")
    private Integer limit;
    private final BookService bookService;
    @GetMapping("/books/recent")
    public String recentBooks(Model model) {
        model.addAttribute("recentBooks", bookService.getRecentBooksPage(RequestDto.builder()
                .from(LocalDate.now().minusMonths(1))
                .to(LocalDate.now())
                .offset(0)
                .limit(limit)
                .build()));
        return "/books/recent";
    }
}
