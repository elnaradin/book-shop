package com.example.MyBookShopApp.config.security;

import com.example.MyBookShopApp.model.enums.StatusType;
import com.example.MyBookShopApp.model.user.UserEntity;
import com.example.MyBookShopApp.repositories.UserRepository;
import com.example.MyBookShopApp.services.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomOauthLoginSuccessHandler implements AuthenticationSuccessHandler {
    private final CookieUtils cookieUtils;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    )
            throws IOException {
        UserEntity user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
        cookieUtils.mergeSelectedBooks(StatusType.KEPT, user);
        cookieUtils.mergeSelectedBooks(StatusType.CART, user);
        response.sendRedirect("/");
    }

}
