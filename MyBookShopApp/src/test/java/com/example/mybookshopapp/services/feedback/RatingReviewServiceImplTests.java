package com.example.mybookshopapp.services.feedback;

import com.example.mybookshopapp.config.security.IAuthenticationFacade;
import com.example.mybookshopapp.dto.ResultDto;
import com.example.mybookshopapp.dto.book.status.ChangeBookStatusDto;
import com.example.mybookshopapp.dto.review.BookRatingDto;
import com.example.mybookshopapp.dto.review.MyReviewDto;
import com.example.mybookshopapp.dto.review.ReviewLikeDto;
import com.example.mybookshopapp.model.book.RatingEntity;
import com.example.mybookshopapp.model.book.review.BookReviewEntity;
import com.example.mybookshopapp.model.book.review.BookReviewLikeEntity;
import com.example.mybookshopapp.model.enums.StatusType;
import com.example.mybookshopapp.repositories.Book2UserRepository;
import com.example.mybookshopapp.repositories.RatingRepository;
import com.example.mybookshopapp.repositories.ReviewLikeRepository;
import com.example.mybookshopapp.repositories.ReviewRepository;
import com.example.mybookshopapp.repositories.UserRepository;
import com.example.mybookshopapp.services.BookStatusService;
import com.example.mybookshopapp.services.FeedbackService;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class RatingReviewServiceImplTests {
    @Autowired
    private FeedbackService ratingReviewService;
    private BookRatingDto bookRatingDto;
    private ReviewLikeDto reviewLikeDto;
    private MyReviewDto reviewDto;
    @MockBean
    private RatingRepository ratingRepositoryMock;
    @MockBean
    private ReviewRepository reviewRepositoryMock;
    @MockBean
    private ReviewLikeRepository reviewLikeRepositoryMock;
    @SpyBean
    private IAuthenticationFacade facadeMock;
    @Autowired
    private  BookStatusService statusService;
    private String slug = "book-bhf-940";
    @Autowired
    private UserRepository userRepository;
    @MockBean
    private Book2UserRepository book2UserRepository;


    @BeforeEach
    void beforeAll() {

        bookRatingDto = new BookRatingDto();
        bookRatingDto.setSlug(slug);
        bookRatingDto.setValue(5);
        reviewLikeDto = new ReviewLikeDto();
        reviewLikeDto.setSlug(slug);
        reviewLikeDto.setValue((short) 1);
        reviewLikeDto.setReviewid("72cbb141-ba61-4af9-bba7-acb8de7e22c9");
        reviewDto = new MyReviewDto();
        reviewDto.setSlug("book-bhf-940");
        reviewDto.setText("qweqweqwqweqeqweqeqeqeqeqwqeqewqewqeqeeeeeeeeeweqwqeqewqeqewqewwqewqwq");
        statusService.changeBookStatus(new ChangeBookStatusDto(List.of(slug), StatusType.PAID));

    }

    @AfterEach
    void afterAll() {
        statusService.changeBookStatus(new ChangeBookStatusDto(List.of(slug), StatusType.UNLINK));
        bookRatingDto = null;
        reviewLikeDto = null;
        reviewDto = null;
    }

    @Test
    @DisplayName("Добавление оценки книге")
    @WithUserDetails("test@email.com")
    void addRating() {
        Mockito.doReturn("test@email.com").when(facadeMock).getCurrentUsername();
        Mockito.doReturn(userRepository.findByEmail("test@email.com").orElseThrow()).when(facadeMock).getPrincipal();
        Mockito.doReturn("PAID").when(book2UserRepository).getCodeByBookSlugAndEmail(slug, "test@email.com");
        ResultDto resultDto = ratingReviewService.addRating(bookRatingDto);
        assertTrue(resultDto.isResult());
        verify(ratingRepositoryMock, times(1))
                .saveAndFlush(any(RatingEntity.class));
    }

    @Test
    @DisplayName("Добавление оценки отзыву")
    @WithUserDetails("test@email.com")
    void addReviewRating() {
        Mockito.doReturn(Optional.of(new BookReviewEntity()))
                .when(reviewRepositoryMock).findByHash(reviewLikeDto.getReviewid());
        Mockito.doReturn(userRepository.findByEmail("test@email.com").orElseThrow()).when(facadeMock).getPrincipal();
        Mockito.doReturn("PAID").when(book2UserRepository).getCodeByBookSlugAndEmail(slug, "test@email.com");
        ResultDto resultDto = ratingReviewService.addReviewRating(reviewLikeDto);
        assertTrue(resultDto.isResult());
        verify(reviewLikeRepositoryMock, times(1))
                .saveAndFlush(any(BookReviewLikeEntity.class));
    }

    @Test
    @DisplayName("Добавление отзыва")
    @WithUserDetails("test@email.com")
    void addBookReview() {
        Mockito.doReturn(userRepository.findByEmail("test@email.com").orElseThrow()).when(facadeMock).getPrincipal();
        Mockito.doReturn("PAID").when(book2UserRepository).getCodeByBookSlugAndEmail(slug, "test@email.com");
        ResultDto resultDto = ratingReviewService.addBookReview(reviewDto);
        assertTrue(resultDto.isResult());
        verify(reviewRepositoryMock, times(1))
                .saveAndFlush(any(BookReviewEntity.class));
    }

}