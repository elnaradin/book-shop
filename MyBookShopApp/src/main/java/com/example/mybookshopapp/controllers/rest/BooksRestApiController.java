package com.example.mybookshopapp.controllers.rest;

import com.example.mybookshopapp.dto.ResultDto;
import com.example.mybookshopapp.dto.book.BooksPageDto;
import com.example.mybookshopapp.dto.book.request.RequestDto;
import com.example.mybookshopapp.dto.book.status.ChangeBookStatusDto;
import com.example.mybookshopapp.dto.review.BookRatingDto;
import com.example.mybookshopapp.dto.review.MyReviewDto;
import com.example.mybookshopapp.dto.review.ReviewLikeDto;
import com.example.mybookshopapp.services.AuthorService;
import com.example.mybookshopapp.services.BookService;
import com.example.mybookshopapp.services.BookStatusService;
import com.example.mybookshopapp.services.FeedbackService;
import com.example.mybookshopapp.services.GenreService;
import com.example.mybookshopapp.services.PaymentService;
import com.example.mybookshopapp.services.SearchService;
import com.example.mybookshopapp.services.TagService;
import com.example.mybookshopapp.services.util.CookieUtils;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Api("book data api")
@RequiredArgsConstructor
@Slf4j
public class BooksRestApiController {
    private final BookService bookService;
    private final SearchService searchService;
    private final AuthorService authorService;
    private final FeedbackService ratingService;

    private final BookStatusService bookStatusService;
    private final GenreService genreService;
    private final TagService tagService;
    private final CookieUtils cookieUtils;
    private final PaymentService paymentService;


    @GetMapping("/books/recommended")
    public BooksPageDto getRecommendedBookPage(RequestDto request) {
        return bookService.getRecommendedBooksPage(request);
    }

    @GetMapping("/books/popular")
    public BooksPageDto getPopularBookPage(RequestDto request) {
        return bookService.getPopularBooksPage(request);
    }

    @GetMapping("/books/recent")
    public BooksPageDto getRecentBookPage(RequestDto request) {
        return bookService.getRecentBooksPage(request);
    }


    @GetMapping("/books/tag/{slug}")
    public BooksPageDto tagPage(RequestDto request) {
        return tagService.getBooksPageByTag(request);
    }

    @GetMapping("/books/genre/{slug}")
    public BooksPageDto genrePage(RequestDto request) {
        return genreService.getBooksPageByGenre(request);
    }

    @GetMapping("/books/author/{slug}")
    public BooksPageDto bookOfAuthorPage(RequestDto request) {
        return authorService.getBooksPageByAuthor(request);
    }

    @PostMapping("/rateBook")
    public ResultDto rateBook(@RequestBody BookRatingDto bookRatingDto) {
        return ratingService.addRating(bookRatingDto);
    }

    @GetMapping("/search/google-books/{searchWord}")
    public BooksPageDto getNextSearchPage(RequestDto request) {
        return searchService.getPageOfGoogleBooksApiSearchResult(request);
    }
    @GetMapping("/search/local/{searchWord}")
    public BooksPageDto getNextLocalSearchPage(RequestDto request) {
        return searchService.getPageOfSearchResultBooks(request);
    }

    @PostMapping("/bookReview")
    public ResultDto addBookReview(@RequestBody MyReviewDto reviewDto) {
        return ratingService.addBookReview(reviewDto);
    }

    @PostMapping("/rateBookReview")
    public ResultDto rateBookReview(@RequestBody ReviewLikeDto reviewRatingDto) {
        return ratingService.addReviewRating(reviewRatingDto);

    }

    @PostMapping("/anonym/changeBookStatus")
    public ResultDto changeBookStatusAnonym(@RequestBody RequestDto request) {
        cookieUtils.addBook2Cookie(request);
        return ResultDto.builder().result(true).build();
    }

    @PostMapping("/changeBookStatus")
    public ResultDto changeBookStatus(@RequestBody ChangeBookStatusDto changeStatusPayload) {
        return bookStatusService.changeBookStatus(changeStatusPayload);
    }

    @GetMapping("/payment")
    public ResultDto handlePay() {
        return paymentService.buyCartItems();
    }
}
