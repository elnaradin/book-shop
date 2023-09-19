package com.example.mybookshopapp.services.feedback;

import com.example.mybookshopapp.config.security.IAuthenticationFacade;
import com.example.mybookshopapp.dto.ResultDto;
import com.example.mybookshopapp.dto.book.RatingDto;
import com.example.mybookshopapp.dto.review.BookRatingDto;
import com.example.mybookshopapp.dto.review.MyReviewDto;
import com.example.mybookshopapp.dto.review.ReviewDto;
import com.example.mybookshopapp.dto.review.ReviewLikeDto;
import com.example.mybookshopapp.errs.ItemNotFoundException;
import com.example.mybookshopapp.model.book.BookEntity;
import com.example.mybookshopapp.model.book.RatingEntity;
import com.example.mybookshopapp.model.book.review.BookReviewEntity;
import com.example.mybookshopapp.model.book.review.BookReviewLikeEntity;
import com.example.mybookshopapp.model.enums.StatusType;
import com.example.mybookshopapp.model.user.UserEntity;
import com.example.mybookshopapp.repositories.Book2UserRepository;
import com.example.mybookshopapp.repositories.BookRepository;
import com.example.mybookshopapp.repositories.RatingRepository;
import com.example.mybookshopapp.repositories.ReviewLikeRepository;
import com.example.mybookshopapp.repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.mybookshopapp.errs.Messages.getMessageForLocale;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackServiceImpl implements FeedbackService {
    private final BookRepository bookRepository;
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
        Optional<BookReviewEntity> review = reviewRepository.findByHash(reviewLikeDto.getReviewid());
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
                    .text(reviewDto.getText())
                    .hash(UUID.randomUUID().toString()).build();
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

    @Override
    @Transactional
    public ResultDto deleteReview(String hash) {
        ResultDto resultDto = new ResultDto();
        long count = reviewRepository.deleteByHash(hash);
        log.info("reviews deleted: " + count);
        if (count > 0){
            resultDto.setResult(true);
            return resultDto;
        }
        resultDto.setError(getMessageForLocale("exceptionMessage.itemNotFound.review"));
        return resultDto;
    }

    private BookEntity getBook(String slug) {
        return bookRepository.findBySlug(slug)
                .orElseThrow(()->new ItemNotFoundException("exceptionMessage.itemNotFound.book"));
    }
}
