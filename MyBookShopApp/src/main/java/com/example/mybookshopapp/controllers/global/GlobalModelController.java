package com.example.mybookshopapp.controllers.global;

import com.example.mybookshopapp.dto.search.SearchWordDto;
import com.example.mybookshopapp.dto.user.SmallUserDto;
import com.example.mybookshopapp.services.BookStatusService;
import com.example.mybookshopapp.services.UserRegService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalModelController {
    private final BookStatusService statusService;

    private final UserRegService userService;
    @ModelAttribute("slugsByStatus")
    public Map<String, List<String>> getCartBlockBooksCount() {
        return statusService.getUserBookSlugs();
    }
    @ModelAttribute("currentUser")
    public SmallUserDto getCurrentUser() {
        return userService.getShortUserInfo();
    }


    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }
    @ModelAttribute("lang")
    public String getCurrentLocale() {
        Locale locale = LocaleContextHolder.getLocale().stripExtensions();
        if (locale.getLanguage().equals("ru")){
           return "ru";
        }
        return "en";
    }
}
