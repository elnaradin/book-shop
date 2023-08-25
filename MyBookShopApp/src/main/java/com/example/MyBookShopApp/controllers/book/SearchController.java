package com.example.MyBookShopApp.controllers.book;

import com.example.MyBookShopApp.annotation.RequestParamsTrackable;
import com.example.MyBookShopApp.dto.book.request.RequestDto;
import com.example.MyBookShopApp.dto.search.SearchWordDto;
import com.example.MyBookShopApp.errs.EmptySearchException;
import com.example.MyBookShopApp.model.book.BookEntity;
import com.example.MyBookShopApp.services.book.BookService;
import com.example.MyBookShopApp.services.search.SearchService;
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
    @Value("${books-batch-size.pool}")
    private Integer limit;
    private final BookService bookService;
    private final SearchService searchService;


    @ModelAttribute("searchResults")
    public List<BookEntity> searchResults() {
        return new ArrayList<>();
    }

    @GetMapping(value = {"/search", "/search/{searchWord}"})
    @RequestParamsTrackable
    public String getSearchResults(
            @PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto,
            Model model
    ) throws EmptySearchException {
        if (searchWordDto != null) {
            model.addAttribute("searchWordDto", searchWordDto);
            model.addAttribute("booksPage", searchService.getPageOfGoogleBooksApiSearchResult(
                    RequestDto.builder()
                            .searchWord(searchWordDto.getExample())
                            .offset(0)
                            .limit(limit)
                            .build()
            ));
            return "/search/index";
        } else {
            throw new EmptySearchException("Поиск по null невозможен");
        }
    }
}
