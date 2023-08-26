package com.example.MyBookShopApp.services.loginAndRegistration;


import com.example.MyBookShopApp.config.security.IAuthenticationFacade;
import com.example.MyBookShopApp.config.security.jwt.JWTUtils;
import com.example.MyBookShopApp.dto.ResultDto;
import com.example.MyBookShopApp.dto.payment.ChangeBalanceDto;
import com.example.MyBookShopApp.dto.payment.yookassa.request.ProfileUpdateDto;
import com.example.MyBookShopApp.dto.security.ContactConfirmationPayload;
import com.example.MyBookShopApp.dto.security.ContactConfirmationResponse;
import com.example.MyBookShopApp.dto.security.RegistrationForm;
import com.example.MyBookShopApp.errs.InsufficientFundsException;
import com.example.MyBookShopApp.errs.UserExistsException;
import com.example.MyBookShopApp.model.enums.StatusType;
import com.example.MyBookShopApp.model.user.UserEntity;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRegServiceImpl implements UserRegService {
    private final UserRolesRepository userRolesRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;
    private final CookieUtils cookieUtils;
    private final HttpServletResponse response;
    private final IAuthenticationFacade facade;


    @Override
    @Transactional
    public UserEntity registerNewUser(RegistrationForm registrationForm) {
        String phone = registrationForm.getPhone().replaceAll("[( )-]", "");
        if (userRepository.existsByEmail(registrationForm.getEmail()) ||
                userRepository.existsByPhone(phone)) {
            log.warn("user already registered");
            throw new UserExistsException();
        }
        UserEntity user = UserEntity.builder().name(registrationForm.getName())
                .email(registrationForm.getEmail())
                .phone(phone)
                .regTime(LocalDateTime.now())
                .password(passwordEncoder.encode(registrationForm.getPass()))
                .roles(List.of(userRolesRepository.findByRole("USER")))
                .build();
        log.info("new user registered with email: " + registrationForm.getEmail());
        UserEntity savedUser = userRepository.save(user);
        cookieUtils.mergeSelectedBooks(StatusType.KEPT, savedUser);
        cookieUtils.mergeSelectedBooks(StatusType.CART, savedUser);
        authenticate(user);
        return user;
    }

    @Override
    @Transactional
    public ContactConfirmationResponse jwtLogin(ContactConfirmationPayload payload) {
        Optional<UserEntity> user = userRepository.findByEmail(payload.getContact());
        if (user.isEmpty()) {
            String phone = payload.getContact().replace("+", "");
            phone = "+" + (phone.startsWith("8") ? phone.replaceFirst("8", "7") : phone);
            user = userRepository.findFirstByPhone(phone);
        }
        if (user.isEmpty()) {
            return null;
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.get().getEmail(),
                payload.getCode()
        );
        authenticationManager.authenticate(authentication);
        return addBooksAndGenerateResponse(user.orElseThrow());
    }

    @Override
    public ContactConfirmationResponse jwtLoginByPhone(ContactConfirmationPayload payload) {
        UserEntity user = userRepository.findFirstByPhone(payload.getContact()).orElseThrow();
        authenticate(user);
        return addBooksAndGenerateResponse(user);
    }

    @Override
    public ContactConfirmationResponse jwtLoginByEmail(ContactConfirmationPayload payload) {
        UserEntity user = userRepository.findByEmail(payload.getContact()).orElseThrow();
        authenticate(user);
        return addBooksAndGenerateResponse(user);
    }

    @Override
    @Transactional
    public ResultDto updateProfile(ProfileUpdateDto updateDto) {
        ResultDto resultDto = new ResultDto();
        UserEntity user = userRepository.findByEmail(facade.getCurrentUsername()).orElseThrow();
        if (updateDto.getPassword().equals(updateDto.getPasswordReply())) {
            user.setEmail(updateDto.getMail());
            if (!updateDto.getPassword().isBlank()) {
                user.setPassword(passwordEncoder.encode(updateDto.getPassword()));
            }
            user.setPhone(updateDto.getPhone());
            user.setName(updateDto.getName());
            replaceAuthenticationAndJwt(user);
            userRepository.save(user);
            resultDto.setResult(true);
            return resultDto;
        }
        resultDto.setResult(false);
        return resultDto;
    }

    private void replaceAuthenticationAndJwt(UserEntity user) {
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities()
        ));
        response.addCookie(cookieUtils.createTokenCookie(jwtUtils.generateToken(user.getEmail()), -1));
    }

    @Override
    @Transactional
    public void changeBalance(ChangeBalanceDto changeBalanceDto) {
        UserEntity user = userRepository.findByEmail(changeBalanceDto.getUsername()).orElseThrow();
        int oldBalance = user.getBalance();
        switch (changeBalanceDto.getOperationType()) {
            case TOP_UP:
                user.setBalance(oldBalance + changeBalanceDto.getAmount());
                break;
            case WRITE_OFF:
                if (oldBalance < changeBalanceDto.getAmount()) {
                    throw new InsufficientFundsException();
                }
                user.setBalance(oldBalance - changeBalanceDto.getAmount());
        }
        user.setHash(null);
        userRepository.save(user);
    }

    private ContactConfirmationResponse addBooksAndGenerateResponse(UserEntity user) {
        cookieUtils.mergeSelectedBooks(StatusType.CART, user);
        cookieUtils.mergeSelectedBooks(StatusType.KEPT, user);
        String jwtToken = jwtUtils.generateToken(user.getEmail());
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult(jwtToken);
        return response;
    }

    private void authenticate(UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authenticationToken);
    }
}
