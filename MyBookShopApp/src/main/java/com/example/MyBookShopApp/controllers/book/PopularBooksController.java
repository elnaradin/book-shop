package com.example.MyBookShopApp.controllers.book;

import com.example.MyBookShopApp.dto.BookDto;
import com.example.MyBookShopApp.dto.SearchWordDto;
import com.example.MyBookShopApp.services.BooksRatingAndPopularityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PopularBooksController {
    @Value("${books-pool-limit.popular}")
    private Integer limit;
    @Value("${universal-initial-offset}")
    private Integer offset;
    private final BooksRatingAndPopularityService ratingAndPopularityService;

    @ModelAttribute("popularBooks")
    public List<BookDto> bookList() {
        return ratingAndPopularityService.getListOfPopularBooks(offset, limit);
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @GetMapping("/books/popular")
    public String popularBooksPage() {
        return "/books/popular";
    }

}
