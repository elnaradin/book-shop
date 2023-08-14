package com.example.MyBookShopApp.services.ratingAndReview;

import com.example.MyBookShopApp.dto.review.ReviewDto;
import com.example.MyBookShopApp.dto.review.ReviewLikeDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource("/application-test.properties")
public class RatingReviewServiceImpl2Tests {
    private final String bookSlug = "book-tsv-614";
    @Autowired
    private RatingReviewServiceImpl ratingReviewService;
    private ReviewLikeDto reviewLikeDto;
    @BeforeEach
    void setUp() {
        reviewLikeDto = new ReviewLikeDto();
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
        Integer lastReviewId = bookReviews.get(bookReviews.size() - 1).getId();
        reviewLikeDto.setReviewid(lastReviewId);
        //add ratings to a review
        addReviewRatings(reviewLikeDto);
        List<ReviewDto> newBookReviews = ratingReviewService.getBookReviews(bookSlug);
        assertEquals(lastReviewId, newBookReviews.get(0).getId());

    }


    void addReviewRatings(ReviewLikeDto reviewLikeDto ){
        for(int i = 2; i < 50; i++){
            ratingReviewService.addReviewRating(reviewLikeDto, "email" + i + "@email.com");
        }
    }
}