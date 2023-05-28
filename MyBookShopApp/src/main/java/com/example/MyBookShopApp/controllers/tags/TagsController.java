package com.example.MyBookShopApp.controllers.tags;

import com.example.MyBookShopApp.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class TagsController {
    @Value("${books-pool-limit.tag}")
    private Integer limit;
    @Value("${universal-initial-offset}")
    private Integer offset;
    private final BookService bookService;


    @GetMapping("/books/tag/{id}")
    public String tagPage(@PathVariable("id") Integer tagId, Model model){
        model.addAttribute("taggedBooks", bookService.getTaggedBooks(tagId, offset, limit));
        return "/tags/index";
    }
}
