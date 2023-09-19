package com.example.mybookshopapp.services.auth;


import com.example.mybookshopapp.config.security.IAuthenticationFacade;
import com.example.mybookshopapp.config.security.jwt.JWTUtils;
import com.example.mybookshopapp.dto.ResultDto;
import com.example.mybookshopapp.dto.book.request.RequestDto;
import com.example.mybookshopapp.dto.payment.ChangeBalanceDto;
import com.example.mybookshopapp.dto.payment.OperationType;
import com.example.mybookshopapp.dto.payment.yookassa.request.ProfileUpdateDto;
import com.example.mybookshopapp.dto.security.ContactConfirmationPayload;
import com.example.mybookshopapp.dto.security.ContactConfirmationResponse;
import com.example.mybookshopapp.dto.security.RegistrationForm;
import com.example.mybookshopapp.dto.user.SmallUserDto;
import com.example.mybookshopapp.dto.user.UserDto;
import com.example.mybookshopapp.dto.user.UserDtoPage;
import com.example.mybookshopapp.errs.InsufficientFundsException;
import com.example.mybookshopapp.errs.UserExistsException;
import com.example.mybookshopapp.mapper.UserMapper;
import com.example.mybookshopapp.model.enums.StatusType;
import com.example.mybookshopapp.model.user.UserContactEntity;
import com.example.mybookshopapp.model.user.UserEntity;
import com.example.mybookshopapp.repositories.UserContactRepository;
import com.example.mybookshopapp.repositories.UserRepository;
import com.example.mybookshopapp.repositories.UserRolesRepository;
import com.example.mybookshopapp.services.contactconfirmation.ContactConfirmationService;
import com.example.mybookshopapp.services.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.Objects;
import java.util.Optional;

import static com.example.mybookshopapp.errs.Messages.getMessageForLocale;

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
    private final UserContactRepository codeRepository;
    private final UserMapper mapper;
    private final ContactConfirmationService contactConfirmationService;
    private static final String TOKEN_COOKIE_NAME = "token";


    @Override
    @Transactional
    public UserEntity registerNewUser(RegistrationForm registrationForm) {
        String phone = registrationForm.getPhone().replaceAll("[( )-]", "");
        if (userRepository.existsByEmail(registrationForm.getEmail()) ||
                userRepository.existsByPhone(phone)) {
            log.warn("user already registered");
            throw new UserExistsException("exceptionMessage.userExists");
        }
        UserEntity user = UserEntity.builder()
                .fullName(registrationForm.getName())
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
        ContactConfirmationResponse contactConfirmationResponse = contactConfirmationService.verifyPassword(payload);
        if (contactConfirmationResponse.getResult() == null) {
            return contactConfirmationResponse;
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                payload.getContact(),
                payload.getCode()
        );
        Authentication authenticate = authenticationManager.authenticate(authentication);
        String token = addBooksAndGenerateJwt((UserEntity) authenticate.getPrincipal());
        response.addCookie(cookieUtils.createSecureHttpOnlyCookie(TOKEN_COOKIE_NAME, token, -1));
        return contactConfirmationResponse;
    }

    @Override
    public ContactConfirmationResponse jwtLoginByPhone(ContactConfirmationPayload payload) {
        ContactConfirmationResponse contactConfirmationResponse = contactConfirmationService.verifyCode(payload);
        if (contactConfirmationResponse.getResult() == null) {
            return contactConfirmationResponse;
        }
        UserEntity user = userRepository.findFirstByPhone(payload.getContact()).orElseThrow();
        authenticate(user);
        return contactConfirmationResponse;
    }

    @Override
    public ContactConfirmationResponse jwtLoginByEmail(ContactConfirmationPayload payload) {
        ContactConfirmationResponse contactConfirmationResponse = contactConfirmationService.verifyCode(payload);
        if (contactConfirmationResponse.getResult() == null) {
            return contactConfirmationResponse;
        }
        UserEntity user = userRepository.findByEmail(payload.getContact()).orElseThrow();
        authenticate(user);
        return contactConfirmationResponse;
    }

    private boolean isApproved(String contact) {
        Optional<UserContactEntity> entity = codeRepository.findTopByContactOrderByCodeTimeDesc(contact);
        if (StringUtils.isBlank(contact)) {
            return true;
        }
        return entity.isPresent() && entity.get().getApproved() != 0;
    }

    @Override
    @Transactional
    public ResultDto updateProfile(ProfileUpdateDto updateDto) {
        ResultDto resultDto = new ResultDto();
        UserEntity user = userRepository.findByEmail(facade.getCurrentUsername()).orElseThrow();
        if (!isApproved(updateDto.getPhone()) || !isApproved(updateDto.getMail())) {
            return resultDto;
        }
        if (updateDto.getPassword().equals(updateDto.getPasswordReply())) {
            user.setEmail(updateDto.getMail());
            if (!updateDto.getPassword().isBlank()) {
                user.setPassword(passwordEncoder.encode(updateDto.getPassword()));
            }
            user.setPhone(updateDto.getPhone());
            user.setFullName(updateDto.getName());
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
        response.addCookie(cookieUtils.createSecureHttpOnlyCookie(TOKEN_COOKIE_NAME, jwtUtils.generateToken(user.getEmail()), -1));
    }

    @Override
    @Transactional
    public void changeBalance(ChangeBalanceDto changeBalanceDto) {
        UserEntity user = userRepository.findByEmail(changeBalanceDto.getUsername()).orElseThrow();
        int oldBalance = user.getBalance();
        if (Objects.requireNonNull(changeBalanceDto.getOperationType()) == OperationType.TOP_UP) {
            user.setBalance(oldBalance + changeBalanceDto.getAmount());
        } else if (changeBalanceDto.getOperationType() == OperationType.WRITE_OFF) {
            if (oldBalance < changeBalanceDto.getAmount()) {
                throw new InsufficientFundsException("exceptionMessage.insufficientFunds");
            }
            user.setBalance(oldBalance - changeBalanceDto.getAmount());
        }
        user.setHash(null);
        userRepository.save(user);
    }

    @Override
    public SmallUserDto getShortUserInfo() {
        if(!facade.isAuthenticated()){
            return  new SmallUserDto();
        }
        UserEntity user = userRepository.findByEmail(facade.getCurrentUsername()).orElseThrow();
        return mapper.toSmallUserDto(user);
    }

    @Override
    public UserDtoPage getUserPage(RequestDto request) {
        Pageable pageRequest = PageRequest.of(request.getOffset(), request.getLimit());
        Page<UserDto> users = userRepository.getUserDtoPageByNameOrEmailOrPhone(request.getSearchWord(), pageRequest);
        return new UserDtoPage(users.getTotalElements(), users.hasNext(), users.getContent());
    }

    @Override
    @Transactional
    public ResultDto deleteUser(String itemId) {
        ResultDto resultDto = new ResultDto();
        long count = userRepository.deleteByEmail(itemId);
        log.info("users deleted: " + count);
        if (count > 0){
            resultDto.setResult(true);
            return resultDto;
        }
        resultDto.setError(getMessageForLocale("exceptionMessage.itemNotFound.user"));
        return resultDto;
    }

    private String addBooksAndGenerateJwt(UserEntity user) {
        cookieUtils.mergeSelectedBooks(StatusType.CART, user);
        cookieUtils.mergeSelectedBooks(StatusType.KEPT, user);
        return jwtUtils.generateToken(user.getEmail());
    }

    private void authenticate(UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authenticationToken);
        String token = addBooksAndGenerateJwt((UserEntity) userDetails);
        response.addCookie(cookieUtils.createSecureHttpOnlyCookie(TOKEN_COOKIE_NAME, token, -1));
    }
}
