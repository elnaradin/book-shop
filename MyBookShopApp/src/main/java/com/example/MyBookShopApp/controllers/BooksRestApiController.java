package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.dto.ResultDto;
import com.example.MyBookShopApp.dto.author.AuthorWithBooksDto;
import com.example.MyBookShopApp.dto.book.BooksPageDto;
import com.example.MyBookShopApp.dto.book.ChangeStatusPayload;
import com.example.MyBookShopApp.dto.genre.BooksByGenreDto;
import com.example.MyBookShopApp.dto.review.BookRatingDto;
import com.example.MyBookShopApp.dto.review.MyReviewDto;
import com.example.MyBookShopApp.dto.review.ReviewRatingDto;
import com.example.MyBookShopApp.dto.search.SearchWordDto;
import com.example.MyBookShopApp.dto.tag.BooksByTagDto;
import com.example.MyBookShopApp.services.AuthorService;
import com.example.MyBookShopApp.services.BookService;
import com.example.MyBookShopApp.services.BookStatusService;
import com.example.MyBookShopApp.services.BooksRatingAndPopularityService;
import com.example.MyBookShopApp.services.ReviewService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Api(description = "book data api")
@RequiredArgsConstructor
@Slf4j
public class BooksRestApiController {
    private final BooksRatingAndPopularityService ratingAndPopularityService;
    private final BookService bookService;
    private final AuthorService authorService;
    private final ReviewService reviewService;

    private final BookStatusService bookStatusService;


    @GetMapping("/books/recommended")
    @ResponseBody
    public BooksPageDto getRecommendedBookPage(@RequestParam("offset") Integer offset,
                                               @RequestParam("limit") Integer limit) {
        return bookService.getPageOfRecommendedBooks(offset, limit);
    }

    @GetMapping("/books/popular")
    @ResponseBody
    public BooksPageDto getPopularBookPage(@RequestParam("offset") Integer offset,
                                           @RequestParam("limit") Integer limit) {
        return ratingAndPopularityService.getListOfPopularBooks(offset, limit);
    }

    @GetMapping("/books/recent")
    @ResponseBody
    public BooksPageDto getRecentBookPage(@RequestParam(value = "from", required = false) String from,
                                          @RequestParam(value = "to", required = false) String to,
                                          @RequestParam(value = "offset", required = false) Integer offset,
                                          @RequestParam(value = "limit", required = false) Integer limit) {

        return bookService.getPageOfRecentBooksByPubDate(from, to, offset, limit);
    }


    @GetMapping("/books/tag/{id}")
    public BooksByTagDto tagPage(@PathVariable("id") Integer tagId,
                                 @RequestParam(value = "offset", required = false) Integer offset,
                                 @RequestParam(value = "limit", required = false) Integer limit) {

        return bookService.getTaggedBooks(tagId, offset, limit);
    }

    @GetMapping("/books/genre/{slug}")
    public BooksByGenreDto genrePage(@PathVariable("slug") String slug,
                                     @RequestParam(value = "offset", required = false) Integer offset,
                                     @RequestParam(value = "limit", required = false) Integer limit) {
        return bookService.getBooksByGenre(slug, offset, limit);
    }

    @GetMapping("/books/author/{slug}")
    public AuthorWithBooksDto bookOfAuthorPage(@PathVariable("slug") String slug,
                                               @RequestParam(value = "offset", required = false) Integer offset,
                                               @RequestParam(value = "limit", required = false) Integer limit) {

        return authorService.getAuthorWithBooks(slug, offset, limit);
    }

    @PostMapping("/rateBook")
    public ResultDto rateBook(@RequestBody BookRatingDto bookRatingDto) {
        return ratingAndPopularityService.addRating(bookRatingDto);
    }

    @GetMapping("/search/{searchWord}")
    public BooksPageDto getNextSearchPage(@RequestParam("offset") Integer offset,
                                          @RequestParam("limit") Integer limit,
                                          @PathVariable(value = "searchWord", required = false)
                                          SearchWordDto searchWordDto) {
        return bookService.getPageOfSearchResultBooks(searchWordDto.getExample(), offset, limit);
    }

    @PostMapping("/bookReview")
    public ResultDto addBookReview(@RequestBody MyReviewDto reviewDto) {

        return reviewService.saveBookReview(reviewDto);

    }

    @PostMapping("/rateBookReview")
    public ResultDto rateBookReview(@RequestBody ReviewRatingDto ratingDto) {
        return reviewService.saveBookReviewRating(ratingDto);

    }

    @PostMapping("/changeBookStatus")
    public ResultDto handleChangeBookStatus(@RequestBody ChangeStatusPayload changeStatusPayload) {
        return bookStatusService.changeBookStatus(changeStatusPayload.getBookIds(),
                changeStatusPayload.getStatus());
    }


}
