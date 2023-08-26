package com.example.MyBookShopApp.controllers.global;

import com.example.MyBookShopApp.dto.ResultDto;
import com.example.MyBookShopApp.errs.EmptySearchException;
import com.example.MyBookShopApp.errs.InsufficientFundsException;
import com.example.MyBookShopApp.errs.ItemNotFoundException;
import com.example.MyBookShopApp.errs.UserExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EmptySearchException.class)
    public String handleEmptySearchException(
            EmptySearchException e,
            RedirectAttributes redirectAttributes
    ) {
        log.warn(e.getLocalizedMessage());
        redirectAttributes.addFlashAttribute("searchError", e);
        return "redirect:/";
    }


    @ExceptionHandler({ItemNotFoundException.class})
    public String handleBooksNotFound(RedirectAttributes attributes) {
        attributes.addFlashAttribute("notFoundError", true);
        return "redirect:/404";
    }


    @ExceptionHandler({AuthenticationException.class})
    public String handleAuthenticationException(RedirectAttributes attributes) {
        attributes.addFlashAttribute("authError", true);
        return "redirect:/signin";
    }

    @ExceptionHandler({UserExistsException.class})
    public String handleRegWithExistingEmail(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("emailExistsError", true);
        return "redirect:/signup";
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request
    ) {
        log.error("Validation error");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResultDto.builder().result(false).build());
    }

    @ExceptionHandler({InsufficientFundsException.class})
    @ResponseBody
    public ResultDto handleInsufficientFunds() {
        return ResultDto.builder()
                .result(false)
                .error("Недостаточно средств")
                .build();
    }
}
