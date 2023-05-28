package com.example.MyBookShopApp.controllers.global;

import com.example.MyBookShopApp.errs.BookNotFoundException;
import com.example.MyBookShopApp.errs.EmptySearchException;
import com.example.MyBookShopApp.errs.GenreNotFoundException;
import com.example.MyBookShopApp.errs.TagNotFoundException;
import com.example.MyBookShopApp.errs.UserNotAuthenticatedException;
import com.example.MyBookShopApp.services.cookie.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final HttpServletResponse response;
    private final CookieUtils cookieUtils;

    @ExceptionHandler(EmptySearchException.class)
    public String handleEmptySearchException(EmptySearchException e,
                                             RedirectAttributes redirectAttributes) {
        log.warn(e.getLocalizedMessage());
        redirectAttributes.addFlashAttribute("searchError", e);
        return "redirect:/";
    }

    @ExceptionHandler({UserNotAuthenticatedException.class})
    public String handleUserNotAuth(Exception e, RedirectAttributes redirectAttributes) {
        log.warn(e.getLocalizedMessage());
        redirectAttributes.addFlashAttribute("signinError", true);
        response.addCookie(cookieUtils.createTokenCookie(null, 0));
        return "redirect:/signin";
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    public String handleUsernameNotFound(Exception e) {
        log.warn(e.getLocalizedMessage());
        response.addCookie(cookieUtils.createTokenCookie(null, 0));
        return "redirect:/";
    }

    @ExceptionHandler({BookNotFoundException.class,
            GenreNotFoundException.class,
            TagNotFoundException.class})
    public String handleItemNotFound(Exception e) {
        log.error(e.getMessage());
        return "redirect:/";
    }
}
