package com.example.MyBookShopApp.config.security.jwt;

import com.example.MyBookShopApp.config.security.BookstoreUserDetails;
import com.example.MyBookShopApp.config.security.BookstoreUserDetailsService;
import com.example.MyBookShopApp.services.util.CookieUtils;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JWTRequestFilter extends OncePerRequestFilter {

    private final BookstoreUserDetailsService userDetailsService;
    private final JWTUtils jwtUtil;
    private final CookieUtils cookieUtils;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token;
        String email;
        Cookie[] cookies = request.getCookies();
        SecurityContext context = SecurityContextHolder.getContext();
        if (cookies == null) {
            filterChain.doFilter(request, response);
            return;
        }
        for (Cookie cookie : cookies) {
            if (!cookie.getName().equals("token")) {
                continue;
            }
            if (StringUtils.isEmpty(cookie.getValue())) {
                break;
            }
            token = cookie.getValue();
            try {
                email = jwtUtil.extractSubject(token);
                BookstoreUserDetails userDetails = (BookstoreUserDetails) userDetailsService
                        .loadUserByUsername(email);
                if (!jwtUtil.validateToken(token, userDetails.getUsername())) {
                    break;
                }
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authenticationToken);
                filterChain.doFilter(request, response);
                return;
            } catch (JwtException | NoSuchElementException ignored) {
                SecurityContextHolder.clearContext();
            }
        }
        try {
            filterChain.doFilter(request, response);
        } catch (AccessDeniedException e) {
            e.printStackTrace();
            response.sendRedirect("/signin");
        }

    }

}
