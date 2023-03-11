package com.example.MyBookShopApp.controllers.book;

import com.example.MyBookShopApp.dto.SearchWordDto;
import com.example.MyBookShopApp.services.BookService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TagsController {
    @Value("${books-pool-limit.tag}")
    private Integer limit;
    @Value("${universal-initial-offset}")
    private Integer offset;
    private final BookService bookService;

    public TagsController(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @GetMapping("/books/tag/{id}")
    public String tagPage(@PathVariable("id") Integer tagId, Model model) {
        model.addAttribute("taggedBooks", bookService.getTaggedBooks(tagId, offset, limit));
        return "/tags/index";
    }
}
