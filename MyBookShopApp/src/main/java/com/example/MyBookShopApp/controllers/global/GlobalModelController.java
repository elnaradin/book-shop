package com.example.MyBookShopApp.controllers.global;

import com.example.MyBookShopApp.dto.search.SearchWordDto;
import com.example.MyBookShopApp.services.bookStatus.BookStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalModelController {
    private final BookStatusService statusService;

    //for authorized
    @ModelAttribute("slugsByStatus")
    public Map<String, List<String>> getCartBlockBooksCount(Authentication authentication) {
        return statusService.getUserBookSlugs(authentication);
    }


    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }
}
