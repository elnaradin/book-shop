package com.example.MyBookShopApp.config.jwt;

import com.example.MyBookShopApp.model.user.UserEntity;
import com.example.MyBookShopApp.repositories.JwtBlackListRepository;
import com.example.MyBookShopApp.services.UserService;
import com.example.MyBookShopApp.services.cookie.CookieUtils;
import com.example.MyBookShopApp.services.security.BookstoreUserDetails;
import com.example.MyBookShopApp.services.security.BookstoreUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JWTRequestFilter extends OncePerRequestFilter {
    @Value("${blacklist.time}")
    private int blTime;
    private final JwtBlackListRepository jwtBlackListRepository;

    private final BookstoreUserDetailsService bookstoreUserDetailsService;
    private final JWTUtils jwtUtil;
    private final CookieUtils cookieUtils;

    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

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
                if (StringUtils.isEmpty(email.trim())) {
                    log.info("jwt subject is empty");
                    break;
                }
                BookstoreUserDetails userDetails = (BookstoreUserDetails) bookstoreUserDetailsService.loadUserByUsername(email);
                if (!jwtUtil.validateToken(token, userDetails.getUsername())) {
                    clearContext(response, context);
                    break;
                }
                setAuthentication(request, context, userDetails, "ROLE_USER");
                log.info("user authenticated with jwt. Roles: "
                        + userService.getCurUserRoles());
                filterChain.doFilter(request, response);
                return;
            } catch (ExpiredJwtException | UsernameNotFoundException e) {
                log.error(e.getLocalizedMessage());
                response.addCookie(cookieUtils.createTokenCookie(null, 0));
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                log.info("clearing expired cookie");
                break;
            }
        }
        if (context.getAuthentication() == null) {
            addEmptyUser(request, response, context);
        }
        filterChain.doFilter(request, response);
    }

    private void clearContext(HttpServletResponse response, SecurityContext context) {
        log.warn("jwt not valid");
        context.setAuthentication(null);
        response.addCookie(cookieUtils.createTokenCookie(null, 0));
    }

    private void addEmptyUser(HttpServletRequest request, HttpServletResponse response, SecurityContext context) {
        log.info("new empty user created");
        UserEntity user = userService.createEmptyUser();
        BookstoreUserDetails userDetails = new BookstoreUserDetails(user);
        Cookie cookie = cookieUtils.createTokenCookie(jwtUtil.generateToken(user.getEmail()), blTime);
        setAuthentication(request, context, userDetails, "ROLE_ANONYMOUS");
        response.addCookie(cookie);
    }


    private static void setAuthentication(HttpServletRequest request, SecurityContext context,
                                          BookstoreUserDetails userDetails, String authority) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, List.of(new SimpleGrantedAuthority(authority)));
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        log.info("user authenticated through filter chain");
        context.setAuthentication(authenticationToken);
    }

}
