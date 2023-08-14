package com.example.MyBookShopApp.config.security.jwt;

import com.example.MyBookShopApp.config.security.BookstoreEmailUserDetails;
import com.example.MyBookShopApp.config.security.BookstoreUserDetailsService;
import com.example.MyBookShopApp.services.util.CookieUtils;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

@Component
@RequiredArgsConstructor
@Slf4j
public class JWTRequestFilter extends OncePerRequestFilter {
    @Value("${blacklist.time-min}")
    private int blTime;

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
        if (cookies != null) {
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
                    BookstoreEmailUserDetails userDetails = (BookstoreEmailUserDetails) userDetailsService
                            .loadUserByUsername(email);
                    if (!jwtUtil.validateToken(token, userDetails.getUsername())) {
                        log.warn("token isn't valid");
                        clearContext(response, context);
                        break;
                    }
                    setAuthentication(request, context, userDetails);
                    filterChain.doFilter(request, response);
                    return;
                } catch (JwtException | UsernameNotFoundException e) {
                    log.error(e.getLocalizedMessage());
                    response.addCookie(cookieUtils.createTokenCookie(null, 0));
                    log.warn("clearing expired cookie");
                    break;
                }
            }
        }
//        addEmptyUser(request, response, context);
        filterChain.doFilter(request, response);
    }

    private void clearContext(HttpServletResponse response, SecurityContext context) {
        log.warn("jwt not valid");
        context.setAuthentication(null);
        response.addCookie(cookieUtils.createTokenCookie(null, 0));
    }


    private static void setAuthentication(
            HttpServletRequest request, SecurityContext context,
            BookstoreEmailUserDetails userDetails
    ) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        context.setAuthentication(authenticationToken);
    }

}
