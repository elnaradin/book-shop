package com.example.MyBookShopApp.services.loginAndRegistration;

import com.example.MyBookShopApp.config.security.jwt.JWTUtils;
import com.example.MyBookShopApp.dto.security.ContactConfirmationPayload;
import com.example.MyBookShopApp.dto.security.ContactConfirmationResponse;
import com.example.MyBookShopApp.dto.security.RegistrationForm;
import com.example.MyBookShopApp.errs.EmailExistsException;
import com.example.MyBookShopApp.model.enums.StatusType;
import com.example.MyBookShopApp.model.user.UserEntity;
import com.example.MyBookShopApp.repositories.Book2UserRepository;
import com.example.MyBookShopApp.repositories.Book2UserTypeRepository;
import com.example.MyBookShopApp.repositories.UserRepository;
import com.example.MyBookShopApp.repositories.UserRolesRepository;
import com.example.MyBookShopApp.services.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRegServiceImpl implements UserRegService {
    private final Book2UserRepository book2UserRepository;
    private final Book2UserTypeRepository book2UserTypeRepository;
    private final UserRolesRepository userRolesRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;
    private final CookieUtils cookieUtils;


    @Override
    @Transactional
    public UserEntity registerNewUser(RegistrationForm registrationForm) {
        if (userRepository.existsByEmail(registrationForm.getEmail())) {
            log.warn("user already registered");
            throw new EmailExistsException("User with this email is already registered");
        }
        UserEntity user = UserEntity.builder().name(registrationForm.getName())
                .email(registrationForm.getEmail())
                .phone(registrationForm.getPhone())
                .regTime(LocalDateTime.now())
                .password(passwordEncoder.encode(registrationForm.getPass()))
                .roles(List.of(userRolesRepository.findByRole("USER")))
                .build();
        log.info("new user registered with email: " + registrationForm.getEmail());
        UserEntity savedUser = userRepository.save(user);
        cookieUtils.mergeSelectedBooks(StatusType.KEPT, savedUser);
        cookieUtils.mergeSelectedBooks(StatusType.CART, savedUser);
        return user;
    }

    @Override
    @Transactional
    public ContactConfirmationResponse jwtLogin(ContactConfirmationPayload payload) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(payload.getContact(), payload.getCode()));
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);
        UserEntity user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
        cookieUtils.mergeSelectedBooks(StatusType.CART, user);
        cookieUtils.mergeSelectedBooks(StatusType.KEPT, user);
        String jwtToken = jwtUtils.generateToken(authentication.getName());
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult(jwtToken);
        return response;
    }


}
