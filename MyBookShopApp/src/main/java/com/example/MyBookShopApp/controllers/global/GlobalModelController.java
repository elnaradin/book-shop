package com.example.MyBookShopApp.controllers.global;

import com.example.MyBookShopApp.dto.HeaderValues;
import com.example.MyBookShopApp.dto.search.SearchWordDto;
import com.example.MyBookShopApp.errs.UserNotAuthenticatedException;
import com.example.MyBookShopApp.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalModelController {
    private final UserService userService;

    @ModelAttribute("header")
    public HeaderValues isAuthorised() throws UserNotAuthenticatedException {
        return userService.findHeaderValues();
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ExceptionHandler({UserNotAuthenticatedException.class})
    public String handleUnauthorizedReview(Exception e, Model model) throws UserNotAuthenticatedException {
        log.warn(e.getLocalizedMessage());
        model.addAttribute("header", userService.findHeaderValues());
        model.addAttribute("searchWordDto", new SearchWordDto());
        return "signin";
    }
}
