package com.example.MyBookShopApp.dto.mappers;

import com.example.MyBookShopApp.dto.author.SmallAuthorsDto;
import com.example.MyBookShopApp.dto.review.ReviewDto;
import com.example.MyBookShopApp.dto.tag.SmallTagsDto;
import com.example.MyBookShopApp.model.book.BookEntity;
import com.example.MyBookShopApp.model.book.links.Book2UserEntity;
import com.example.MyBookShopApp.model.book.links.Book2UserTypeEntity;
import com.example.MyBookShopApp.model.user.UserEntity;
import com.example.MyBookShopApp.repositories.Book2UserTypeRepository;
import com.example.MyBookShopApp.repositories.BookRepository;
import com.example.MyBookShopApp.services.cookie.CookieUtils;
import com.example.MyBookShopApp.services.security.BookstoreUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MapperUtils {
    private final HttpServletRequest request;
    private final CookieUtils cookieUtils;

    private final BookRepository bookRepository;
    private final Book2UserTypeRepository book2UserTypeRepository;

    public String getAuthorNames(Integer bookId) {
        List<String> names = bookRepository.getAuthorNames(bookId);
        if (CollectionUtils.isEmpty(names)) {
            return "authors not found";
        }
        return names.size() > 1 ? names.get(0).concat(" и прочие") : names.get(0);
    }

    public List<SmallAuthorsDto> getAuthors(Integer bookId) {
        List<SmallAuthorsDto> authorsDtos = bookRepository.getAuthorNamesAndSlugsById(bookId);
        return CollectionUtils.isEmpty(authorsDtos) ? new ArrayList<>() : authorsDtos;
    }

    public int calculateDiscountPrice(Integer price, Integer discount) {
        return Math.toIntExact(Math.round(price
                * (1.0 - (double) discount / 100)));
    }

    public String getBookStatus(Integer bookId) {

        BookstoreUserDetails principal = (BookstoreUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<String> status = bookRepository.findBookStatusById(bookId, principal.getBookstoreUser().getId());
        return status.orElse("UNLINK");
    }

    public Integer calculateBooksRating(Integer bookId) {
        Integer bookRatingById = bookRepository.getBookRatingById(bookId);
        return bookRatingById == null ? 0 : bookRatingById;
    }

    public Map<String, Integer> getRatingsByStar(Integer bookId) {
        return bookRepository.countRatingsByStar(bookId);
    }

    public Integer countAllRatingsByBookId(Integer bookId) {
        return bookRepository.countRatingsById(bookId);
    }

    public List<SmallTagsDto> getTags(Integer bookId) {
        return bookRepository.getTagsById(bookId);
    }

    public List<ReviewDto> getReviews(Integer bookId) {
        return bookRepository.getReviewsById(bookId);
    }

    public ArrayList<Book2UserEntity> getBook2UserEntities(String status, List<Integer> bookIds, UserEntity user) {
        List<BookEntity> bookEntityByIdIn = bookRepository.findBookEntityByIdIn(bookIds);
        Book2UserTypeEntity typeEntity = book2UserTypeRepository.findBook2UserTypeEntityByCodeIs(status);
        ArrayList<Book2UserEntity> book2UserEntityList = new ArrayList<>();
        for (BookEntity book : bookEntityByIdIn) {


            Book2UserEntity book2UserEntity = new Book2UserEntity();
            book2UserEntity.setUser(user);
            book2UserEntity.setBook(book);
            book2UserEntity.setTime(LocalDateTime.now());
            book2UserEntity.setType(typeEntity);

            book2UserEntityList.add(book2UserEntity);
        }
        return book2UserEntityList;
    }
}
