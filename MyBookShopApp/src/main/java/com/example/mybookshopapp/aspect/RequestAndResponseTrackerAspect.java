package com.example.mybookshopapp.aspect;

import com.example.mybookshopapp.dto.book.BooksPageDto;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
@Slf4j
public class RequestAndResponseTrackerAspect {
    @AfterReturning(value = "allGetBooksPageRestControllerMethods() || allAdminControllerMethods()", returning = "result")
    public void trackResponseAdvice(JoinPoint joinPoint, BooksPageDto result) {
        String message;
        if (result != null && result.getBooks() != null) {
            message = joinPoint.getSignature().getName() + " result contains: " + result.getBooks().size() + " books";
        } else {
            message = joinPoint.getSignature().getName() + " result contains no books";
        }
        log.info(message);
    }

    @Before(value = "allGetBooksPageRestControllerMethods() || " +
            "@annotation(com.example.mybookshopapp.annotation.RequestParamsTrackable) ||" +
            "allAdminControllerMethods()")
    public void trackRequestParams(JoinPoint joinPoint) {
        log.info(joinPoint.getSignature().getName() + " args: " + Arrays.toString(joinPoint.getArgs()));
    }

    @Pointcut(value = "within(com.example.mybookshopapp.controllers.rest.BooksRestApiController)")
    public void allGetBooksPageRestControllerMethods() {
    }
    @Pointcut(value = "within(com.example.mybookshopapp.controllers.shop.AdminController)")
    public void allAdminControllerMethods() {
    }

}
