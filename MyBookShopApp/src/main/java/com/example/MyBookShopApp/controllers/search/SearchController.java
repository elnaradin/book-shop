package com.example.MyBookShopApp.controllers.search;

import com.example.MyBookShopApp.dto.search.SearchWordDto;
import com.example.MyBookShopApp.errs.EmptySearchException;
import com.example.MyBookShopApp.model.book.BookEntity;
import com.example.MyBookShopApp.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SearchController {
    @Value("${books-pool-limit.search}")
    private Integer limit;
    @Value("${universal-initial-offset}")
    private Integer offset;
    private final BookService bookService;


    @ModelAttribute("searchResults")
    public List<BookEntity> searchResults() {
        return new ArrayList<>();
    }

    @GetMapping(value = {"/search", "/search/{searchWord}"})
    public String getSearchResults(@PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto,
                                   Model model) throws EmptySearchException {
        if (searchWordDto != null) {
            model.addAttribute("searchWordDto", searchWordDto);
            model.addAttribute("searchResults", bookService
                    .getPageOfSearchResultBooks(searchWordDto.getExample(), offset, limit));
            model.addAttribute("booksFoundAmount", bookService.countFoundBooks(searchWordDto.getExample()));
            return "/search/index";
        } else {
            throw new EmptySearchException("Поиск по null невозможен");
        }

    }

}