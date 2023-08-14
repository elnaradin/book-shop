package com.example.MyBookShopApp.aspect;

import com.example.MyBookShopApp.dto.book.BooksPageDto;
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
    @AfterReturning(value = "allGetBooksPageRestControllerMethods()", returning = "result")
    public void trackResponseAdvice(JoinPoint joinPoint, BooksPageDto result) {
        log.info(joinPoint.getSignature().getName() + " response contains: " + result.getBooks().size() + " books");
    }

    @Before(value = "allGetBooksPageRestControllerMethods() || " +
            "@annotation(com.example.MyBookShopApp.annotation.RequestParamsTrackable)")
    public void trackRequestParams(JoinPoint joinPoint) {
        log.info(joinPoint.getSignature().getName() + " args: " + Arrays.toString(joinPoint.getArgs()));
    }

    @Pointcut(value = "within(com.example.MyBookShopApp.controllers.rest.BooksRestApiController)")
    public void allGetBooksPageRestControllerMethods() {
    }

}
