package com.example.MyBookShopApp.services.ratingAndReview;

import com.example.MyBookShopApp.dto.ResultDto;
import com.example.MyBookShopApp.dto.review.BookRatingDto;
import com.example.MyBookShopApp.dto.review.MyReviewDto;
import com.example.MyBookShopApp.dto.review.ReviewLikeDto;
import com.example.MyBookShopApp.model.book.RatingEntity;
import com.example.MyBookShopApp.model.book.review.BookReviewEntity;
import com.example.MyBookShopApp.model.book.review.BookReviewLikeEntity;
import com.example.MyBookShopApp.repositories.RatingRepository;
import com.example.MyBookShopApp.repositories.ReviewLikeRepository;
import com.example.MyBookShopApp.repositories.ReviewRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class RatingReviewServiceImplTests {



    @Autowired
    private RatingReviewService ratingReviewService;
    private BookRatingDto bookRatingDto;
    private ReviewLikeDto reviewLikeDto;
    private MyReviewDto reviewDto;
    @MockBean
    private RatingRepository ratingRepositoryMock;
    @MockBean
    private ReviewRepository reviewRepositoryMock;
    @MockBean
    private ReviewLikeRepository reviewLikeRepositoryMock;


    @BeforeEach
    void beforeAll() {
        bookRatingDto = new BookRatingDto();
        bookRatingDto.setSlug("book-bhf-940");
        bookRatingDto.setValue(5);
        reviewLikeDto = new ReviewLikeDto();
        reviewLikeDto.setValue((short) 1);
        reviewLikeDto.setReviewid(1);
        reviewDto = new MyReviewDto();
        reviewDto.setSlug("book-bhf-940");
        reviewDto.setText("qweqweqwqweqeqweqeqeqeqeqwqeqewqewqeqeeeeeeeeeweqwqeqewqeqewqewwqewqwq");

    }

    @AfterEach
    void afterAll() {
        bookRatingDto = null;
        reviewLikeDto = null;
        reviewDto = null;
    }

    @Test
    @DisplayName("Добавление оценки книге")
    void addRating() {
        Authentication authentication = mock(Authentication.class);
        Mockito.doReturn("test@email.com").when(authentication).getName();
        ResultDto resultDto = ratingReviewService.addRating(bookRatingDto, authentication);
        assertTrue(resultDto.isResult());
        verify(ratingRepositoryMock, times(1))
                .saveAndFlush(any(RatingEntity.class));
    }

    @Test
    @DisplayName("Добавление оценки отзыву")
    void addReviewRating() {
        Mockito.doReturn(Optional.of(new BookReviewEntity()))
                .when(reviewRepositoryMock).findById(reviewLikeDto.getReviewid());
        ResultDto resultDto = ratingReviewService.addReviewRating(reviewLikeDto, "test@email.com");
        assertTrue(resultDto.isResult());
        verify(reviewLikeRepositoryMock, times(1))
                .saveAndFlush(any(BookReviewLikeEntity.class));
    }

    @Test
    @DisplayName("Добавление отзыва")
    void addBookReview() {
        Authentication authentication = mock(Authentication.class);
        Mockito.doReturn("test@email.com").when(authentication).getName();
        ResultDto resultDto = ratingReviewService.addBookReview(reviewDto, authentication);
        assertTrue(resultDto.isResult());
        verify(reviewRepositoryMock, times(1))
                .saveAndFlush(any(BookReviewEntity.class));
    }

}