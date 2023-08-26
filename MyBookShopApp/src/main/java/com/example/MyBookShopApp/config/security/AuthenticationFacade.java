package com.example.MyBookShopApp.config.security;

import com.example.MyBookShopApp.model.user.UserEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade implements IAuthenticationFacade {
    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public UserEntity getPrincipal() {
        return (UserEntity) getAuthentication().getPrincipal();
    }

    @Override
    public String getCurrentUsername() {
        return getAuthentication().getName();
    }

    @Override
    public boolean isAuthenticated() {
        return getAuthentication() != null && !(getAuthentication() instanceof AnonymousAuthenticationToken);
    }

}
