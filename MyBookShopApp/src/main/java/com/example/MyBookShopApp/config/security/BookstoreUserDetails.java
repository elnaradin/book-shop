package com.example.MyBookShopApp.config.security;

import com.example.MyBookShopApp.model.user.UserEntity;
import com.example.MyBookShopApp.model.user.UserRoleEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BookstoreUserDetails implements UserDetails {
    private final Integer id;
    private final String fullName;
    private final String password;
    private final String email;
    private final String phone;
    private final Integer balance;
    private final List<UserRoleEntity> roles;

    public BookstoreUserDetails(UserEntity user) {
        this.id = user.getId();
        this.fullName = user.getName();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.balance = user.getBalance();
        this.roles = user.getRoles();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getRole()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
