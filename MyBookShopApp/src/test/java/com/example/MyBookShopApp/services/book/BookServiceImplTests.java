package com.example.MyBookShopApp.services.book;

import com.example.MyBookShopApp.dto.book.BooksPageDto;
import com.example.MyBookShopApp.dto.book.ChangeStatusPayload;
import com.example.MyBookShopApp.dto.request.RequestDto;
import com.example.MyBookShopApp.model.enums.StatusType;
import com.example.MyBookShopApp.services.bookStatus.BookStatusService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
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

    private ChangeStatusPayload changeStatusPayload;
    private final String popularBookSlug = "book-mna-449";
    @SpyBean
    private BookServiceImpl bookServiceSpy;



    @BeforeEach
    void setUp() {
        requestDto = RequestDto.builder().limit(6).offset(0).build();
        changeStatusPayload = new ChangeStatusPayload();
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
    private void changeUsersBookStatus(ChangeStatusPayload changeStatusPayload) {
        for (int i = 2; i < 50; i++) {
            statusService.changeBookStatus(
                    changeStatusPayload,
                    "email" + i + "@email.com"
            );
        }
    }


}