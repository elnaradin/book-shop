package com.example.mybookshopapp.controllers.book;

import com.example.mybookshopapp.annotation.RequestParamsTrackable;
import com.example.mybookshopapp.dto.book.request.RequestDto;
import com.example.mybookshopapp.dto.search.SearchWordDto;
import com.example.mybookshopapp.errs.EmptySearchException;
import com.example.mybookshopapp.model.book.BookEntity;
import com.example.mybookshopapp.services.BookService;
import com.example.mybookshopapp.services.SearchService;
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

    @GetMapping(value = {"/search/google-books", "/search/google-books/{searchWord}"})
    @RequestParamsTrackable
    public String getLocalSearchResults(
            @PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto,
            Model model
    ) throws EmptySearchException {
        if (searchWordDto != null) {
            model.addAttribute("searchWordDto", searchWordDto);
            model.addAttribute("booksPage", searchService.getPageOfGoogleBooksApiSearchResult(
                    RequestDto.builder().searchWord(searchWordDto.getExample())
                            .offset(0).limit(limit).build()
            ));
            model.addAttribute("type", "search-google");
            return "/search/index";
        } else {
            throw new EmptySearchException("exceptionMessage.emptySearchInput");
        }
    }

    @GetMapping(value = {"/search/local", "/search/local/{searchWord}"})
    @RequestParamsTrackable
    public String getGoogleBooksSearchResults(
            @PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto,
            Model model
    ) throws EmptySearchException {
        if (searchWordDto != null) {
            model.addAttribute("searchWordDto", searchWordDto);
            model.addAttribute("booksPage", searchService.getPageOfSearchResultBooks(
                    RequestDto.builder().searchWord(searchWordDto.getExample())
                            .offset(0).limit(limit).build()
            ));
            model.addAttribute("type", "search-local");
            return "/search/index";
        } else {
            throw new EmptySearchException("exceptionMessage.emptySearchInput");
        }
    }

}
