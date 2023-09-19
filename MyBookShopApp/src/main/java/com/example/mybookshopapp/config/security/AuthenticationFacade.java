package com.example.mybookshopapp.config.security;

import com.example.mybookshopapp.model.user.UserEntity;
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
        if(!isAuthenticated()){
            return null;
        }
        return (UserEntity) getAuthentication().getPrincipal();
    }

    @Override
    public String getCurrentUsername() {
        if(!isAuthenticated()){
            return null;
        }
        return getAuthentication().getName();
    }

    @Override
    public boolean isAuthenticated() {
        return getAuthentication() != null && !(getAuthentication() instanceof AnonymousAuthenticationToken);
    }

    @Override
    public boolean isAdmin() {
        return getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(a-> a.getAuthority().equals("ROLE_ADMIN"));
    }

}
