package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.dto.ChangeStatusPayload;
import com.example.MyBookShopApp.dto.ResultDto;
import com.example.MyBookShopApp.dto.SearchWordDto;
import com.example.MyBookShopApp.dto.book.BookDto;
import com.example.MyBookShopApp.dto.book.BookRatingDto;
import com.example.MyBookShopApp.dto.book.ReviewDto;
import com.example.MyBookShopApp.dto.book.ReviewRatingDto;
import com.example.MyBookShopApp.dto.bookCollections.BooksPageDto;
import com.example.MyBookShopApp.services.AuthorService;
import com.example.MyBookShopApp.services.BookService;
import com.example.MyBookShopApp.services.BookStatusService;
import com.example.MyBookShopApp.services.BooksRatingAndPopularityService;
import com.example.MyBookShopApp.services.ReviewService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api")
@Api(description = "book data api")
@RequiredArgsConstructor
public class BooksRestApiController {
    private final BooksRatingAndPopularityService ratingAndPopularityService;
    private final BookService bookService;
    private final AuthorService authorService;
    private final ReviewService reviewService;

    private final BookStatusService bookStatusService;
    @Value(value = "${min-review-length}")
    private int minReviewLength;


    @GetMapping("/books/recommended")
    @ResponseBody
    public BooksPageDto getRecommendedBookPage(@RequestParam("offset") Integer offset,
                                               @RequestParam("limit") Integer limit) {
        List<BookDto> books = bookService.getPageOfRecommendedBooks(offset, limit);
        return new BooksPageDto(books);
    }

    @GetMapping("/books/popular")
    @ResponseBody
    public BooksPageDto getPopularBookPage(@RequestParam("offset") Integer offset,
                                           @RequestParam("limit") Integer limit) {
        List<BookDto> books = ratingAndPopularityService.getListOfPopularBooks(offset, limit);
        return new BooksPageDto(books);
    }

    @GetMapping("/books/recent")
    @ResponseBody
    public BooksPageDto getRecentBookPage(@RequestParam(value = "from", required = false) String from,
                                          @RequestParam(value = "to", required = false) String to,
                                          @RequestParam(value = "offset", required = false) Integer offset,
                                          @RequestParam(value = "limit", required = false) Integer limit) throws ParseException {
        List<BookDto> books = bookService.getPageOfRecentBooksByPubDate(from, to, offset, limit);
        return new BooksPageDto(books);
    }

    @GetMapping("/books/tag/{id}")
    public BooksPageDto tagPage(@PathVariable("id") Integer tagId,
                                @RequestParam(value = "offset", required = false) Integer offset,
                                @RequestParam(value = "limit", required = false) Integer limit) {
        List<BookDto> books = bookService.getTaggedBooks(tagId, offset, limit).getBooks();
        return new BooksPageDto(books);
    }

    @GetMapping("/books/genre/{slug}")
    public BooksPageDto genrePage(@PathVariable("slug") String slug,
                                  @RequestParam(value = "offset", required = false) Integer offset,
                                  @RequestParam(value = "limit", required = false) Integer limit) {
        List<BookDto> books = bookService.getBooksByGenre(slug, offset, limit).getBooks();
        return new BooksPageDto(books);
    }

    @GetMapping("/books/author/{slug}")
    public BooksPageDto bookOfAuthorPage(@PathVariable("slug") String slug,
                                         @RequestParam(value = "offset", required = false) Integer offset,
                                         @RequestParam(value = "limit", required = false) Integer limit) {
        List<BookDto> books = authorService.getAuthorWithBooks(slug, offset, limit).getBooks();
        return new BooksPageDto(books);
    }

    @PostMapping("/rateBook")
    public ResultDto rateBook(@RequestBody BookRatingDto bookRatingDto) {
        ResultDto result = new ResultDto();
        if (bookRatingDto.getValue() == 0) {
            return result;
        }
        ratingAndPopularityService.addRating(bookRatingDto.getBookId(), bookRatingDto.getValue());

        result.setResult(true);
        return result;
    }

    @GetMapping("/search/{searchWord}")
    public BooksPageDto getNextSearchPage(@RequestParam("offset") Integer offset,
                                          @RequestParam("limit") Integer limit,
                                          @PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto) {
        List<BookDto> books = bookService.getPageOfSearchResultBooks(searchWordDto.getExample(), offset, limit);
        Integer booksCount = bookService.countFoundBooks(searchWordDto.getExample());
        BooksPageDto booksPageDto = new BooksPageDto(books);
        booksPageDto.setCount(booksCount);
        return booksPageDto;
    }

    @PostMapping("/bookReview")
    public ResultDto addBookReview(@RequestBody ReviewDto reviewDto) {
        ResultDto resultDto = new ResultDto();
        if (reviewDto.getText().length() < minReviewLength) {
            resultDto.setError("Отзыв слишком короткий. Напишите, пожалуйста, более развёрнутый отзыв");
        } else {
            reviewService.saveBookReview(reviewDto.getBookId(), reviewDto.getText());
            resultDto.setResult(true);
        }
        return resultDto;
    }

    @PostMapping("/rateBookReview")
    public ResultDto rateBookReview(@RequestBody ReviewRatingDto ratingDto) {
        ResultDto resultDto = new ResultDto();
        reviewService.saveBookReviewRating(ratingDto.getReviewid(), ratingDto.getValue());
        resultDto.setResult(true);
        return resultDto;
    }

    @PostMapping("/changeBookStatus")
    public ResultDto handleChangeBookStatus(@RequestBody ChangeStatusPayload changeStatusPayload) {
        bookStatusService.changeBookStatus(changeStatusPayload.getBookIds(), changeStatusPayload.getStatus());
        ResultDto resultDto = new ResultDto();
        resultDto.setResult(true);
        return resultDto;

    }

}
