package com.example.mybookshopapp.controllers.rest;

import com.example.mybookshopapp.dto.book.status.ChangeBookStatusDto;
import com.example.mybookshopapp.dto.book.request.RequestDto;
import com.example.mybookshopapp.model.enums.StatusType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
class BooksRestApiControllerTests {
    @Autowired
    private MockMvc mockMvc;
    private RequestDto requestDto;
    @Autowired
    private ObjectMapper objectMapper;
    private String bookSlug;
    private ChangeBookStatusDto payload;

    private MockHttpServletRequest request = new MockHttpServletRequest();

    @BeforeEach
    void setUp() {
        requestDto = new RequestDto();
        bookSlug = "book-tsv-614";
        requestDto.setStatus(StatusType.CART);
        requestDto.setSlug(bookSlug);
        payload = new ChangeBookStatusDto();
        payload.setBookIds(List.of(bookSlug));
        payload.setStatus(StatusType.CART);
    }

    @AfterEach
    void tearDown() {
        bookSlug = null;
        requestDto = null;
        payload = null;
    }

    @Test
    @DisplayName("Помещение книги в корзину не авторизованному пользователю и ее удаление из нее")
    void changeBookStatusAnonym() throws Exception {
        mockMvc.perform(post("/api/anonym/changeBookStatus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().value(StatusType.CART.getCookieName(), containsString(bookSlug)));
        //удаление
        mockMvc.perform(post("/api/anonym/changeBookStatus")
                .cookie(new Cookie(StatusType.CART.getCookieName(), bookSlug))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().value(StatusType.CART.getCookieName(), not(containsString(bookSlug))));
    }

    @Test
    @WithUserDetails("test@email.com")
    @DisplayName("Помещение книги в корзину авторизованному пользователю и ее удаление из нее")
    void changeBookStatus() throws Exception {
        mockMvc.perform(post("/api/changeBookStatus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andDo(print())
                .andExpect(status().isOk());
        //удаление
        payload.setStatus(StatusType.UNLINK);
        mockMvc.perform(post("/api/changeBookStatus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andDo(print())
                .andExpect(status().isOk());

    }
}