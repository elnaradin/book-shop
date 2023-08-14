package com.example.MyBookShopApp.config.security;

import com.example.MyBookShopApp.model.user.UserEntity;
import com.example.MyBookShopApp.repositories.UserRepository;
import com.example.MyBookShopApp.repositories.UserRolesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {
    private final UserRolesRepository userRolesRepository;
    private final UserRepository userRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        String email = oidcUser.getAttributes().get("email").toString();
        UserEntity user = getUserOrCreateNew(email, oidcUser);
        return new CustomOidcUser(oidcUser, user);
    }
    @Transactional
    public UserEntity getUserOrCreateNew(String email, OidcUser oidcUser) {
        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    UserEntity newUser = UserEntity.builder()
                            .name(oidcUser.getFullName())
                            .email(email)
                            .regTime(LocalDateTime.now())
                            .roles(List.of(userRolesRepository.findByRole("USER")))
                            .build();

                    log.info("New user registered through OAuth2");
                    return userRepository.save(newUser);
                });
    }
}
