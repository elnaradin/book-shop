package com.example.mybookshopapp.controllers.global;

import com.example.mybookshopapp.dto.ResultDto;
import com.example.mybookshopapp.errs.EmptySearchException;
import com.example.mybookshopapp.errs.FileDownloadLimitReached;
import com.example.mybookshopapp.errs.InsufficientFundsException;
import com.example.mybookshopapp.errs.ItemNotFoundException;
import com.example.mybookshopapp.errs.UserExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({EmptySearchException.class})
    public String handleEmptySearchException(
            EmptySearchException e,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        log.warn(e.getLocalizedMessage());
        redirectAttributes.addFlashAttribute("searchError", e);
        return "redirect:" + request.getHeader(HttpHeaders.REFERER);
    }


    @ExceptionHandler({ItemNotFoundException.class})
    public String handleBooksNotFound(ItemNotFoundException ex, RedirectAttributes attributes) {
        attributes.addFlashAttribute("notFoundError", true);
        attributes.addFlashAttribute("errMessage", ex.getLocalizedMessage());
        return "redirect:/404";
    }

    @ExceptionHandler({UserExistsException.class})
    public String handleRegWithExistingEmail(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("userExistsError", true);
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
    public ResultDto handleInsufficientFunds(InsufficientFundsException ex) {
        return ResultDto.builder()
                .result(false)
                .error(ex.getLocalizedMessage())
                .build();
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ex.getMessage());
        return super.handleBindException(ex, headers, status, request);
    }

    @ExceptionHandler({FileDownloadLimitReached.class})
    public String handleFileDownloadLimitReached (HttpServletRequest request, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("limitReached", true);
        return "redirect:"+ request.getHeader(HttpHeaders.REFERER);
    }
}
