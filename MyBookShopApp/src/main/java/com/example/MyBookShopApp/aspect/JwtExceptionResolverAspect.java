package com.example.MyBookShopApp.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtExceptionResolverAspect {
    private final HttpServletResponse response;

    @AfterThrowing(throwing = "ex", value = "@annotation(com.example.MyBookShopApp.annotation.JwtExceptionResolvable)")
    public void resolveJwtException(JoinPoint joinPoint, Exception ex) throws IOException {
        log.warn(joinPoint.getSignature().getName() + " threw " + ex.getClass().getSimpleName());
        SecurityContextHolder.clearContext();
        response.addCookie(new Cookie("token", null) {{
            setMaxAge(0);
        }});
        response.sendRedirect("/signin");
    }
}
