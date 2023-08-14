package com.example.MyBookShopApp.controllers.book;

import com.example.MyBookShopApp.dto.book.BooksPageDto;
import com.example.MyBookShopApp.dto.request.RequestDto;
import com.example.MyBookShopApp.dto.tag.TagDtoProjection;
import com.example.MyBookShopApp.services.book.BookService;
import com.example.MyBookShopApp.services.tag.TagService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Api
public class MainPageController {
    @Value("${books-batch-size.slider}")
    private Integer limit;

    private final BookService bookService;
    private final TagService tagService;

    @ModelAttribute("recommended")
    public BooksPageDto recommendedBooks() {
           return bookService.getRecommendedBooksPage(
                    getDefaultRequest());
    }

    @ModelAttribute("recent")
    public BooksPageDto recentBooks() {
        return bookService.getRecentBooksPage(
                getDefaultRequest()
        );
    }


    @ModelAttribute("popular")
    public BooksPageDto popularBooks() {
        return bookService.getPopularBooksPage(
               getDefaultRequest()
        );
    }

    @ModelAttribute("tagCloud")
    public List<TagDtoProjection> tagCloud() {
        return tagService.getTagsList();
    }

    @GetMapping("/")
    public String mainPage() {
        return "index";
    }
    private RequestDto getDefaultRequest(){
        return  RequestDto.builder()
                .offset(0)
                .limit(limit).build();
    }
}
