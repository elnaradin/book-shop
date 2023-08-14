package com.example.MyBookShopApp.controllers.user;

import com.example.MyBookShopApp.dto.security.ContactConfirmationPayload;
import com.example.MyBookShopApp.dto.security.RegistrationForm;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;
import java.util.Random;

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;
    private RegistrationForm registrationForm;
    @Autowired
    private ObjectMapper objectMapper;
    private ContactConfirmationPayload payload;


    @BeforeEach
    void setUp() {

        registrationForm = new RegistrationForm();
        registrationForm.setPass("1234567");
        registrationForm.setEmail("test" + new Random().nextInt(1000) + "@mail.org");
        registrationForm.setPhone("+7 (123) " + (10 + new Random().nextInt(89)) + "-78-98");
        registrationForm.setName("Miranda");
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
    void handleUserRegistration() throws Exception {
        mockMvc.perform(post("/reg")
                        .params(convert(registrationForm)))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("Логин по почте")
    void handleEmailLogin() throws Exception {
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    @DisplayName("Логин по номеру телефона")
    void handlePhoneLogin() throws Exception {
        payload.setContact("+7 (999) 999-99-99");
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().value("token", not(emptyOrNullString())));
    }
    MultiValueMap<String, String> convert(Object obj) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        Map<String, String> maps = objectMapper.convertValue(obj, new TypeReference<>() {
        });
        parameters.setAll(maps);

        return parameters;
    }

    @Test
    void logout() throws Exception {
        mockMvc.perform(get("/logout"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().value("token",  emptyOrNullString()));

    }
}