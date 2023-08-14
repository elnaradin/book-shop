package com.example.MyBookShopApp.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Aspect
@Slf4j
public class MethodExecDurationTrackerAspect {
    @Around("allGetPageMethods() || @annotation(com.example.MyBookShopApp.annotation.DurationTrackable)")
    public Object trackExecDurationAdvice(ProceedingJoinPoint joinPoint) {
        String method = joinPoint.getSignature().getName();
        log.info(method + " duration tracking begins");
        long durationMills = new Date().getTime();
        Object proceed = null;
        try {
            proceed = joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        log.info(method + " took: " +
                (new Date().getTime() - durationMills) + " mills");
        return proceed;

    }

    @Pointcut(value = "execution(* get*Page*(..))")
    public void allGetPageMethods() {
    }
}
