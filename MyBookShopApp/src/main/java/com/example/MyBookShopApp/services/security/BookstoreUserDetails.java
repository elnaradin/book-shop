package com.example.MyBookShopApp.services.security;

import com.example.MyBookShopApp.model.user.User2RoleEntity;
import com.example.MyBookShopApp.model.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BookstoreUserDetails implements UserDetails {

    private final UserEntity bookstoreUser;


    public UserEntity getBookstoreUser() {
        return bookstoreUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return bookstoreUser.getUser2role().stream()
                .map(User2RoleEntity::getRole).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return bookstoreUser.getPassword();
    }

    @Override
    public String getUsername() {
        return bookstoreUser.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
