package com.example.MyBookShopApp.services.security;

import com.example.MyBookShopApp.config.jwt.JWTUtils;
import com.example.MyBookShopApp.dto.security.ContactConfirmationPayload;
import com.example.MyBookShopApp.dto.security.ContactConfirmationResponse;
import com.example.MyBookShopApp.dto.security.RegistrationForm;
import com.example.MyBookShopApp.errs.UserNotAuthenticatedException;
import com.example.MyBookShopApp.model.enums.UserRoles;
import com.example.MyBookShopApp.model.user.User2RoleEntity;
import com.example.MyBookShopApp.model.user.UserEntity;
import com.example.MyBookShopApp.repositories.User2RoleRepository;
import com.example.MyBookShopApp.repositories.UserRepository;
import com.example.MyBookShopApp.repositories.UserRolesRepository;
import com.example.MyBookShopApp.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookstoreUserRegister {
    private final User2RoleRepository user2RoleEntityRepository;
    private final UserRolesRepository userRolesRepository;

    private final HttpServletResponse response;

    private final UserRepository bookstoreUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtil;
    private final UserService userService;


    public void registerNewUser(RegistrationForm registrationForm, Model model) throws UserNotAuthenticatedException {

        if (bookstoreUserRepository.findBookstoreUserByEmail(registrationForm.getEmail()).isEmpty()) {
            UserEntity user = userService.getCurUser();
            user.setName(registrationForm.getName());
            user.setEmail(registrationForm.getEmail());
            user.setPhone(registrationForm.getPhone());
            user.setRegTime(LocalDateTime.now());
            user.setPassword(passwordEncoder.encode(registrationForm.getPass()));
            bookstoreUserRepository.save(user);
            User2RoleEntity user2Role = new User2RoleEntity();
            user2Role.setUser(user);
            user2Role.setRole(userRolesRepository.findById(UserRoles.USER.getId()).get());
            user.getUser2role().add(user2Role);
            user2RoleEntityRepository.save(user2Role);
            log.info("new user registered");
            model.addAttribute("regOk", true);
        } else {
            model.addAttribute("regOk", false);
        }
    }

    public ContactConfirmationResponse jwtLogin(ContactConfirmationPayload payload)
            throws UserNotAuthenticatedException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(payload.getContact(),
                        payload.getCode())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtUtil.generateToken(userService.getCurUser().getEmail());
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult(jwtToken);
        Cookie cookie = new Cookie("token", jwtToken);
        this.response.addCookie(cookie);
        return response;
    }
}
