package com.example.mybookshopapp.services.book;

import com.example.mybookshopapp.config.security.AuthenticationFacade;
import com.example.mybookshopapp.dto.book.BooksPageDto;
import com.example.mybookshopapp.dto.book.request.RequestDto;
import com.example.mybookshopapp.dto.book.status.ChangeBookStatusDto;
import com.example.mybookshopapp.model.enums.StatusType;
import com.example.mybookshopapp.repositories.UserRepository;
import com.example.mybookshopapp.services.bookstatus.BookStatusService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class BookServiceImplTests {

    @Autowired
    private BookServiceImpl bookService;

    private RequestDto requestDto;
    @Autowired
    private BookStatusService statusService;

    private ChangeBookStatusDto changeStatusPayload;
    private final String popularBookSlug = "book-zkb-340";
    @SpyBean
    private BookServiceImpl bookServiceSpy;
    @MockBean
    AuthenticationFacade facadeSpy;
    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    void setUp() {
        requestDto = RequestDto.builder().limit(6).offset(0).build();
        changeStatusPayload = new ChangeBookStatusDto();
        changeStatusPayload.setBookIds(List.of(popularBookSlug));
        changeStatusPayload.setStatus(StatusType.CART);
    }

    @AfterEach
    void tearDown() {
        requestDto = null;
        changeStatusPayload = null;

    }

    @Test
    @DisplayName("Получение списка рекомендаций")
    void getRecommendedBooksPage() {
        String authorName = "Howard O'Doogan";
        //books by Howard O'Doogan
        List<String> bookSlugs = List.of("book-asg-221", "book-rqj-173", "book-cif-695", "book-xvd-106");
        BooksPageDto recommendedBooksPage = bookService.getRecommendedBooksPage(requestDto);
        assertNotNull(recommendedBooksPage.getBooks());
        assertEquals(6, recommendedBooksPage.getBooks().size());
        assertThat(
                recommendedBooksPage.getBooks().get(0).getAuthors(),
                not(containsString(authorName))
        );
        doReturn(bookSlugs)
                .when(bookServiceSpy).getSlugsToExclude();
        BooksPageDto newRecommendedBooksPage = bookServiceSpy.getRecommendedBooksPage(requestDto);
        assertNotEquals(
                recommendedBooksPage.getBooks().get(0).getAuthors(),
                newRecommendedBooksPage.getBooks().get(0).getAuthors()
        );
        assertThat(
                newRecommendedBooksPage.getBooks().get(0).getAuthors(),
                containsString(authorName)
        );
    }

    @Test
    @DisplayName("Получение списка популярных книг")
    @WithUserDetails("test@email.com")
    void getPopularBooksPage() {
        BooksPageDto popularBooksPage = bookService.getPopularBooksPage(requestDto);
        assertNotNull(popularBooksPage.getBooks());
        assertEquals(6, popularBooksPage.getBooks().size());
        assertNotEquals(popularBookSlug, popularBooksPage.getBooks().get(0).getSlug());
        changeUsersBookStatus(changeStatusPayload);
        BooksPageDto newPopularBooksPage = bookService.getPopularBooksPage(requestDto);
        assertEquals(popularBookSlug, newPopularBooksPage.getBooks().get(0).getSlug());
        //Удаление книги 50 пользователей из корзины
        changeStatusPayload.setStatus(StatusType.UNLINK);
        changeUsersBookStatus(changeStatusPayload);
    }

    //Помещение книги 50 пользователям в корзину
    private void changeUsersBookStatus(ChangeBookStatusDto changeStatusPayload) {
        for (int i = 2; i < 70; i++) {
            Mockito.doReturn(userRepository.findByEmail("email"+ i +"@email.com").orElseThrow()).when(facadeSpy).getPrincipal();
            Mockito.doReturn("email" + i + "@email.com").when(facadeSpy).getCurrentUsername();
            statusService.changeBookStatus(changeStatusPayload);
        }
    }


}