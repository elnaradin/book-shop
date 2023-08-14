package com.example.MyBookShopApp.config.security;

import com.example.MyBookShopApp.config.security.jwt.JWTUtils;
import com.example.MyBookShopApp.services.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    private final CookieUtils cookieUtils;
    private final JWTUtils jwtUtil;

    @Override
    public void onLogoutSuccess(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            Authentication authentication
    ) throws IOException {

        Cookie[] cookies = httpServletRequest.getCookies();
        Cookie cookie = cookieUtils.getCookieByName(cookies, "token");
        if (cookie == null || StringUtils.isEmpty(cookie.getValue())) {
            return;
        }
        jwtUtil.blacklistJwt(cookie.getValue());
        httpServletResponse.sendRedirect("/");
    }

}
