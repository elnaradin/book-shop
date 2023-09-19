package com.example.mybookshopapp.controllers.book;

import com.example.mybookshopapp.dto.book.request.RequestDto;
import com.example.mybookshopapp.services.book.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PopularBooksController {
    @Value("${books-batch-size.pool}")
    private Integer limit;
    private final BookService bookService;

    @GetMapping("/books/popular")
    public String popularBooksPage(Model model) {
        model.addAttribute("popularBooks", bookService.getPopularBooksPage(
                RequestDto.builder()
                        .offset(0)
                        .limit(limit)
                        .build()
        ));
        return "/books/popular";
    }

}
