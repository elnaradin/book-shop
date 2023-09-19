package com.example.mybookshopapp.controllers.book;

import com.example.mybookshopapp.dto.book.request.RequestDto;
import com.example.mybookshopapp.services.tag.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class TagController {
    @Value("${books-batch-size.pool}")
    private Integer limit;
    private final TagService tagService;

    @GetMapping("/books/tag/{slug}")
    public String getTagPage(@PathVariable("slug") String tagSlug, Model model) {
        model.addAttribute("tag", tagService.getShortTagInfo(tagSlug));
        model.addAttribute("tagCloud", tagService.getTagsList());
        model.addAttribute("booksPage", tagService.getBooksPageByTag(
                RequestDto.builder().slug(tagSlug).offset(0).limit(limit).build()
        ));
        return "/tags/index";
    }
}
