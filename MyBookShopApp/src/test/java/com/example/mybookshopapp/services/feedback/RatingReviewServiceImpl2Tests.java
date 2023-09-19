package com.example.mybookshopapp.services.feedback;

import com.example.mybookshopapp.config.security.IAuthenticationFacade;
import com.example.mybookshopapp.dto.review.ReviewDto;
import com.example.mybookshopapp.dto.review.ReviewLikeDto;
import com.example.mybookshopapp.repositories.Book2UserRepository;
import com.example.mybookshopapp.repositories.UserRepository;
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
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource("/application-test.properties")
 class RatingReviewServiceImpl2Tests {
    private final String bookSlug = "book-tsv-614";
    @Autowired
    private FeedbackServiceImpl ratingReviewService;
    private ReviewLikeDto reviewLikeDto;


    @MockBean
    private Book2UserRepository book2UserRepositoryMock;
    @SpyBean
    private IAuthenticationFacade facadeSpy;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {

        reviewLikeDto = new ReviewLikeDto();
        reviewLikeDto.setSlug(bookSlug);
        reviewLikeDto.setValue((short) 1);
    }

    @AfterEach
    void tearDown() {
        //remove ratings from review
        reviewLikeDto.setValue((short) 0);
        addReviewRatings(reviewLikeDto);
        reviewLikeDto = null;
    }

    @Test
    @WithUserDetails("test@email.com")
    @DisplayName("Расчет рейтинга отзыва")
    void getBookReviews() {

        List<ReviewDto> bookReviews = ratingReviewService.getBookReviews(bookSlug);
        assertNotNull(bookReviews);
        assertThat(bookReviews.size(), greaterThan(0));
        String lastReviewId = bookReviews.get(bookReviews.size() - 1).getHash();
        reviewLikeDto.setReviewid(lastReviewId);
        //add ratings to a review
        addReviewRatings(reviewLikeDto);
        List<ReviewDto> newBookReviews = ratingReviewService.getBookReviews(bookSlug);
        assertEquals(lastReviewId, newBookReviews.get(0).getHash());

    }


    void addReviewRatings(ReviewLikeDto reviewLikeDto) {
        Mockito.doReturn("PAID").when(book2UserRepositoryMock).getCodeByBookSlugAndEmail(bookSlug, "test@email.com");
        for (int i = 2; i < 50; i++) {
            Mockito.doReturn(userRepository.findByEmail("email"+ i +"@email.com").orElseThrow()).when(facadeSpy).getPrincipal();
            ratingReviewService.addReviewRating(reviewLikeDto);
        }
    }
}
