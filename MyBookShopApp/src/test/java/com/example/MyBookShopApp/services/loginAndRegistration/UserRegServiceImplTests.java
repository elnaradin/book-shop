package com.example.MyBookShopApp.services.loginAndRegistration;

import com.example.MyBookShopApp.config.security.jwt.JWTUtils;
import com.example.MyBookShopApp.dto.security.ContactConfirmationPayload;
import com.example.MyBookShopApp.dto.security.ContactConfirmationResponse;
import com.example.MyBookShopApp.dto.security.RegistrationForm;
import com.example.MyBookShopApp.errs.EmailExistsException;
import com.example.MyBookShopApp.model.user.UserEntity;
import com.example.MyBookShopApp.repositories.UserRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserRegServiceImplTests {
    @Autowired
    private  JWTUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private RegistrationForm registrationForm;
    private ContactConfirmationPayload payload;

    @MockBean
    private UserRepository userRepositoryMock;
    @Autowired
    private UserRegServiceImpl userRegService;
    @MockBean
    private AuthenticationManager authenticationManager;


    @BeforeEach
    void setUp() {
        registrationForm = new RegistrationForm();
        registrationForm.setEmail("test@test.test");
        registrationForm.setName("Tester");
        registrationForm.setPass("test");
        registrationForm.setPhone("7878787878");
        payload = new ContactConfirmationPayload();
        payload.setCode("1234567");
        payload.setContact("test@email.com");
    }

    @AfterEach
    void tearDown() {
        registrationForm = null;
        payload = null;
    }

    @Test
    @DisplayName("Регистрация нового пользователя")
    void registerNewUser() {
        UserEntity user = userRegService.registerNewUser(registrationForm);
        assertNotNull(user);
        assertTrue(passwordEncoder.matches(registrationForm.getPass(), user.getPassword()));
        assertTrue(CoreMatchers.is(user.getPhone()).matches(registrationForm.getPhone()));
        assertTrue(CoreMatchers.is(user.getName()).matches(registrationForm.getName()));
        assertTrue(CoreMatchers.is(user.getEmail()).matches(registrationForm.getEmail()));
        Mockito.verify(userRepositoryMock, Mockito.times(1))
                .save(Mockito.any(UserEntity.class));
    }

    @Test
    @DisplayName("Регистрация зарегистрированного пользователя")
    void registerNewUserFail() {
        Mockito.doReturn(true)
                .when(userRepositoryMock)
                .existsByEmail(registrationForm.getEmail());
        assertThrows(EmailExistsException.class, () -> userRegService.registerNewUser(registrationForm));
    }

    @Test
    @DisplayName("Получение токена при входе")
    void jwtLogin() {
        Mockito.doReturn(Optional.of(new UserEntity()))
                .when(userRepositoryMock)
                .findByEmail(payload.getContact());
        Mockito.doReturn(new UsernamePasswordAuthenticationToken(payload.getContact(), passwordEncoder.encode(payload.getCode())))
                .when(authenticationManager)
                .authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(), payload.getCode()));


        ContactConfirmationResponse response = userRegService.jwtLogin(payload);
        String token = response.getResult();
        assertEquals(payload.getContact(), jwtUtils.extractSubject(token));
    }


}