package com.example.MyBookShopApp.services.security;

import com.example.MyBookShopApp.model.enums.UserRoles;
import com.example.MyBookShopApp.model.user.User2RoleEntity;
import com.example.MyBookShopApp.model.user.UserEntity;
import com.example.MyBookShopApp.model.user.UserRoleEntity;
import com.example.MyBookShopApp.repositories.User2RoleRepository;
import com.example.MyBookShopApp.repositories.UserRepository;
import com.example.MyBookShopApp.repositories.UserRolesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class CustomOidcUserService extends OidcUserService {
    private final UserRolesRepository userRolesRepository;
    private final User2RoleRepository user2RoleRepository;
    private final UserRepository userRepository;

    public CustomOidcUserService(UserRepository userRepository,
                                 User2RoleRepository user2RoleRepository,
                                 UserRolesRepository userRolesRepository) {
        this.userRepository = userRepository;
        this.user2RoleRepository = user2RoleRepository;
        this.userRolesRepository = userRolesRepository;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        String email = oidcUser.getAttributes().get("email").toString();
        UserEntity user = createUserIfNoExist(email, oidcUser);

        CustomOidcUser newUser = new CustomOidcUser(oidcUser, user);
        newUser.setEmail(email);
        newUser.setName(user.getName());
        log.info("CustomOidcUser set in context");
        return newUser;
    }

    public UserEntity createUserIfNoExist(String email, OidcUser oidcUser) {
        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    UserEntity newUser = new UserEntity();
                    newUser.setEmail(email);
                    newUser.setName(oidcUser.getFullName());
                    newUser.setRegTime(LocalDateTime.now());
                    userRepository.save(newUser);
                    User2RoleEntity user2Role = new User2RoleEntity();
                    UserRoleEntity role = userRolesRepository.findById(UserRoles.USER.getId()).get();
                    user2Role.setRole(role);
                    user2Role.setUser(newUser);
                    user2RoleRepository.save(user2Role);
                    log.info("New user registered through OAuth2");
                    return newUser;
                });
    }
}
