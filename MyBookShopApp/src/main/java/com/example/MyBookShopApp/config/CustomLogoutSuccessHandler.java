package com.example.MyBookShopApp.config;

import com.example.MyBookShopApp.config.jwt.JWTUtils;
import com.example.MyBookShopApp.services.cookie.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
@Slf4j
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    private final CookieUtils cookieUtils;
    private final JWTUtils jwtUtil;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Authentication authentication) throws IOException {
        Cookie cookie = cookieUtils.getCookieByName(httpServletRequest, "token");
        if (cookie == null) {
            return;
        }
        jwtUtil.blacklistJwt(cookie.getValue());
        log.info("jwt blacklisted after logout");
        httpServletResponse.sendRedirect("/signin");
    }
}
