package com.example.MyBookShopApp.config.security;

import com.example.MyBookShopApp.model.user.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
public class CustomOidcUser implements OidcUser {
    private final OidcUser oidcUser;
    private final UserEntity bookstoreUser;
    private final String email;
    private final String fullName;
    private final Integer balance;
    private final String phone;

    public CustomOidcUser(OidcUser oidcUser, UserEntity bookstoreUser) {
        this.bookstoreUser = bookstoreUser;
        this.oidcUser = oidcUser;
        this.balance = bookstoreUser.getBalance();
        this.email = bookstoreUser.getEmail();
        this.fullName = bookstoreUser.getName();
        this.phone = bookstoreUser.getPhone();
    }

    @Override
    public Map<String, Object> getClaims() {
        return oidcUser.getClaims();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return oidcUser.getUserInfo();
    }

    @Override
    public OidcIdToken getIdToken() {
        return oidcUser.getIdToken();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oidcUser.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oidcUser.getAuthorities();
    }

    @Override
    public String getName() {
        return bookstoreUser.getEmail();
    }
}
