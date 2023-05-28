package com.example.MyBookShopApp.config;

import com.example.MyBookShopApp.config.jwt.JWTUtils;
import com.example.MyBookShopApp.services.security.CustomOidcUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {
    private final JWTUtils jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException {
        CustomOidcUser principal = (CustomOidcUser) authentication.getPrincipal();
        String token = jwtUtil.generateToken(principal.getBookstoreUser().getEmail());
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        httpServletResponse.addCookie(cookie);
        httpServletResponse.sendRedirect("/my");
    }
}
