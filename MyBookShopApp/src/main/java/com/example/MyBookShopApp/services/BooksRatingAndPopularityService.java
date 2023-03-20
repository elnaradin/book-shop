package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.dto.book.BookDto;
import com.example.MyBookShopApp.model.book.BookEntity;
import com.example.MyBookShopApp.model.book.RatingEntity;
import com.example.MyBookShopApp.model.book.links.Book2RatingEntity;
import com.example.MyBookShopApp.model.user.UserEntity;
import com.example.MyBookShopApp.repositories.Book2RatingRepository;
import com.example.MyBookShopApp.repositories.BookRepository;
import com.example.MyBookShopApp.repositories.RatingRepository;
import com.example.MyBookShopApp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BooksRatingAndPopularityService {
    private final BookService bookService;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final RatingRepository ratingRepository;
    private final Book2RatingRepository book2RatingRepository;

    public List<BookDto> getListOfPopularBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        List<BookEntity> bookEntities = bookRepository.findPopularBooks(nextPage).getContent();

        return bookService.createBookList(bookEntities);
    }

    public Integer getBookRatingAmount(String slug) {
        return ratingRepository.getBookRatingAmount(slug);
    }

    public List<Integer> getRatingAmountByStars(String bookSlug) {
        List<Integer> ratingsByStars = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            ratingsByStars.add(ratingRepository.getBookRatingAmountByStars(i, bookSlug));
        }
        return ratingsByStars;
    }

    public void addRating(Integer bookId, Integer value) {
        Optional<BookEntity> book = bookRepository.findById(bookId);
        if (book.isEmpty()) {
            throw new NullPointerException();
        }
        RatingEntity rating;
        Optional<RatingEntity> ratingOptional = ratingRepository
                .findRatingEntityByUserIdAndBookId(1, book.get().getId());
        ratingOptional.ifPresent(ratingRepository::delete);
        rating = new RatingEntity();
        UserEntity user = userRepository.findById(1).get(); //finds a user by id 1 for now
        Book2RatingEntity book2RatingEntity = new Book2RatingEntity();
        book2RatingEntity.setBook(book.get());

        rating.setValue(value);
        rating.setUser(user);
        book2RatingEntity.setRating(rating);
        ratingRepository.save(rating);
        book2RatingRepository.save(book2RatingEntity);
    }
}
