package com.example.mybookshopapp.config.security.oauth2;

import com.example.mybookshopapp.model.enums.ContactType;
import com.example.mybookshopapp.model.user.UserContactEntity;
import com.example.mybookshopapp.model.user.UserEntity;
import com.example.mybookshopapp.repositories.UserContactRepository;
import com.example.mybookshopapp.repositories.UserRepository;
import com.example.mybookshopapp.repositories.UserRolesRepository;
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
    private final UserContactRepository contactRepository;

    @Override
    @Transactional
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        UserEntity user = getUserOrCreateNew(oidcUser);
        user.setOidcUser(oidcUser);
        return user;
    }


    public UserEntity getUserOrCreateNew(OidcUser oidcUser) {
        String email = oidcUser.getAttributes().get("email").toString();
        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    UserEntity newUser = UserEntity.builder()
                            .fullName(oidcUser.getFullName())
                            .email(email)
                            .regTime(LocalDateTime.now())
                            .roles(List.of(userRolesRepository.findByRole("USER")))
                            .build();
                    log.info("New user registered through OAuth2");
                    userRepository.save(newUser);
                    //set email approved
                    UserContactEntity contact = new UserContactEntity("", 0, newUser, email, ContactType.EMAIL);
                    contact.setApproved(1);
                    contactRepository.save(contact);
                    return newUser;
                });
    }
}
