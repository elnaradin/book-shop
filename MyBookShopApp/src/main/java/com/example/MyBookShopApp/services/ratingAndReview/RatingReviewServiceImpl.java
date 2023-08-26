package com.example.MyBookShopApp.services.ratingAndReview;

import com.example.MyBookShopApp.config.security.IAuthenticationFacade;
import com.example.MyBookShopApp.dto.ResultDto;
import com.example.MyBookShopApp.dto.book.RatingDto;
import com.example.MyBookShopApp.dto.review.BookRatingDto;
import com.example.MyBookShopApp.dto.review.MyReviewDto;
import com.example.MyBookShopApp.dto.review.ReviewDto;
import com.example.MyBookShopApp.dto.review.ReviewLikeDto;
import com.example.MyBookShopApp.errs.ItemNotFoundException;
import com.example.MyBookShopApp.model.book.BookEntity;
import com.example.MyBookShopApp.model.book.RatingEntity;
import com.example.MyBookShopApp.model.book.review.BookReviewEntity;
import com.example.MyBookShopApp.model.book.review.BookReviewLikeEntity;
import com.example.MyBookShopApp.model.enums.StatusType;
import com.example.MyBookShopApp.model.user.UserEntity;
import com.example.MyBookShopApp.repositories.Book2UserRepository;
import com.example.MyBookShopApp.repositories.BookRepository;
import com.example.MyBookShopApp.repositories.RatingRepository;
import com.example.MyBookShopApp.repositories.ReviewLikeRepository;
import com.example.MyBookShopApp.repositories.ReviewRepository;
import com.example.MyBookShopApp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RatingReviewServiceImpl implements RatingReviewService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewRepository reviewRepository;
    private final IAuthenticationFacade facade;
    private final Book2UserRepository book2UserRepository;

    @Override
    public RatingDto getBookRating(String slug) {
        return bookRepository.getBookRatingsCountBySlug(slug);
    }

    @Override
    public List<ReviewDto> getBookReviews(String slug) {
        if (facade.getAuthentication() == null || facade.getAuthentication() instanceof AnonymousAuthenticationToken) {
            return reviewRepository.getReviewListBySlug(slug);
        }
        return reviewRepository.getReviewListBySlugAndEmail(slug, facade.getCurrentUsername());
    }

    @Override
    @Transactional
    public ResultDto addRating(BookRatingDto bookRatingDto) {
        ResultDto resultDto = new ResultDto();
        if (!checkPurchase(bookRatingDto.getSlug())) {
            resultDto.setResult(false);
            return resultDto;
        }
        UserEntity user = facade.getPrincipal();
        BookEntity book = getBook(bookRatingDto.getSlug());
        if (!ratingRepository.existsByUserAndBook(user, book)) {
            RatingEntity rating = RatingEntity.builder()
                    .value(bookRatingDto.getValue())
                    .user(user)
                    .book(book)
                    .build();
            ratingRepository.saveAndFlush(rating);
        } else {
            ratingRepository.updateValueByUserAndBook(bookRatingDto.getValue(), user, book);
        }
        resultDto.setResult(true);
        return resultDto;
    }

    private boolean checkPurchase(String slug) {
        String statusType = book2UserRepository.getCodeByBookSlugAndEmail(slug, facade.getCurrentUsername());
        return statusType.equals(StatusType.PAID.toString())  || statusType.equals(StatusType.ARCHIVED.toString()) ;
    }

    @Override
    @Transactional
    public ResultDto addReviewRating(ReviewLikeDto reviewLikeDto) {
        ResultDto resultDto = new ResultDto();
        if (!checkPurchase(reviewLikeDto.getSlug())) {
            resultDto.setResult(false);
            return resultDto;
        }
        UserEntity user = facade.getPrincipal();
        Optional<BookReviewEntity> review = reviewRepository.findById(reviewLikeDto.getReviewid());
        if (!reviewLikeRepository.existsByReviewAndUser(review.orElseThrow(), user)) {
            BookReviewLikeEntity reviewLike = BookReviewLikeEntity.builder()
                    .review(review.get())
                    .user(user)
                    .value(reviewLikeDto.getValue())
                    .time(LocalDateTime.now())
                    .build();
            reviewLikeRepository.saveAndFlush(reviewLike);
        } else {
            reviewLikeRepository.updateTimeAndValueByUserAndReview(
                    LocalDateTime.now(),
                    reviewLikeDto.getValue(),
                    user,
                    review.get()
            );
        }
        resultDto.setResult(true);
        return resultDto;
    }

    @Override
    @Transactional
    public ResultDto addBookReview(MyReviewDto reviewDto) {
        ResultDto resultDto = new ResultDto();
        if (!checkPurchase(reviewDto.getSlug())) {
            resultDto.setResult(false);
            return resultDto;
        }
        BookEntity book = getBook(reviewDto.getSlug());
        UserEntity user = facade.getPrincipal();
        if (!reviewRepository.existsByBookAndUser(book, user)) {
            BookReviewEntity review = BookReviewEntity.builder()
                    .book(book)
                    .user(user)
                    .time(LocalDateTime.now())
                    .text(reviewDto.getText()).build();
            reviewRepository.saveAndFlush(review);
        } else {
            reviewRepository.updateTextAndTimeByBookAndUser(
                    reviewDto.getText(),
                    LocalDateTime.now(),
                    book,
                    user
            );
        }
        resultDto.setResult(true);
        return resultDto;
    }

    @Override
    public Integer getBookRatingOfCurrentUser(String slug) {
        return ratingRepository.getRatingByBookSlugAndUser(slug, facade.getPrincipal());
    }

    private BookEntity getBook(String slug) {
        return bookRepository.findBySlug(slug)
                .orElseThrow(ItemNotFoundException::new);
    }
}
