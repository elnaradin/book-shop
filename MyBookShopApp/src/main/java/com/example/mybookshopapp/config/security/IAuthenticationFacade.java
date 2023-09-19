package com.example.mybookshopapp.config.security;

import com.example.mybookshopapp.model.user.UserEntity;
import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {
    Authentication getAuthentication();
    UserEntity getPrincipal();
    String getCurrentUsername();

    boolean isAuthenticated();

    boolean isAdmin();
}
