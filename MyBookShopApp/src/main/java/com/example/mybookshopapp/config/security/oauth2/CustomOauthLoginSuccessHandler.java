package com.example.mybookshopapp.config.security.oauth2;

import com.example.mybookshopapp.config.security.IAuthenticationFacade;
import com.example.mybookshopapp.model.enums.StatusType;
import com.example.mybookshopapp.model.user.UserEntity;
import com.example.mybookshopapp.services.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomOauthLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final CookieUtils cookieUtils;
    private final IAuthenticationFacade facade;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    )
            throws IOException {
        UserEntity user = facade.getPrincipal();
        cookieUtils.mergeSelectedBooks(StatusType.KEPT, user);
        cookieUtils.mergeSelectedBooks(StatusType.CART, user);
        response.sendRedirect("/");
    }

}
