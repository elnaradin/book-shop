package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.dto.ResultDto;
import com.example.MyBookShopApp.dto.book.BooksPageDto;
import com.example.MyBookShopApp.dto.review.BookRatingDto;
import com.example.MyBookShopApp.errs.BookNotFoundException;
import com.example.MyBookShopApp.errs.UserNotAuthenticatedException;
import com.example.MyBookShopApp.model.book.BookEntity;
import com.example.MyBookShopApp.model.book.RatingEntity;
import com.example.MyBookShopApp.model.book.links.Book2RatingEntity;
import com.example.MyBookShopApp.model.user.UserEntity;
import com.example.MyBookShopApp.repositories.Book2RatingRepository;
import com.example.MyBookShopApp.repositories.BookRepository;
import com.example.MyBookShopApp.repositories.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BooksRatingAndPopularityService {
    private final BookService bookService;
    private final UserService userService;
    private final BookRepository bookRepository;
    private final RatingRepository ratingRepository;
    private final Book2RatingRepository book2RatingRepository;

    public BooksPageDto getListOfPopularBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        List<BookEntity> bookEntities = bookRepository.findPopularBooks(nextPage).getContent();
        return new BooksPageDto(21, bookService.createBookList(bookEntities));
    }


    public ResultDto addRating(BookRatingDto bookRatingDto) throws UserNotAuthenticatedException, BookNotFoundException {
        ResultDto result = new ResultDto();
        if (bookRatingDto.getValue() == 0) {
            return result;
        }
        Optional<BookEntity> book = bookRepository.findById(bookRatingDto.getBookId());
        if (book.isEmpty()) {
            throw new BookNotFoundException("No book was found in database");
        }
        RatingEntity rating;
        UserEntity user = userService.getCurUser();
        Optional<RatingEntity> ratingOptional = ratingRepository
                .findRatingEntityByUserIdAndBookId(user.getId(), book.get().getId());
        ratingOptional.ifPresent(ratingRepository::delete);
        rating = new RatingEntity();
        Book2RatingEntity book2RatingEntity = new Book2RatingEntity();
        book2RatingEntity.setBook(book.get());

        rating.setValue(bookRatingDto.getValue());
        rating.setUser(user);
        book2RatingEntity.setRating(rating);
        ratingRepository.save(rating);
        book2RatingRepository.save(book2RatingEntity);
        result.setResult(true);
        return result;
    }
}
