package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.dto.BookDto;
import com.example.MyBookShopApp.model.book.BookEntity;
import com.example.MyBookShopApp.model.book.links.Book2UserEntity;
import com.example.MyBookShopApp.model.book.links.Book2UserTypeEntity;
import com.example.MyBookShopApp.model.enums.StatusType;
import com.example.MyBookShopApp.model.user.UserEntity;
import com.example.MyBookShopApp.repositories.Book2UserRepository;
import com.example.MyBookShopApp.repositories.Book2UserTypeRepository;
import com.example.MyBookShopApp.repositories.BookRepository;
import com.example.MyBookShopApp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BookStatusService {
    @Value("${user-id}")
    private Integer temporaryUserId;
    private final String cookiePath = "/books";
    private final String cookieName = "cartContents";
    private final BookRepository bookRepository;
    private final BookService bookService;
    private final Book2UserRepository book2UserRepository;
    private final Book2UserTypeRepository book2UserTypeRepository;
    private final UserRepository userRepository;

    public List<BookDto> getPostponedBooks() {
        List<BookEntity> bookEntities = bookRepository
                .findBookEntitiesByUserAndStatus(temporaryUserId, /** for now user is with id 1 **/
                        StatusType.KEPT.toString());


        return bookService.createBookList(bookEntities);
    }

    @Transactional
    public void removeBookFromPostponed(String slug) {
        book2UserRepository.deleteBook2UserEntityByBookSlug(slug);
    }

    public boolean areTherePostponed() {
        return book2UserRepository.existsByTypeCodeAndUserId(StatusType.KEPT.toString(), 1);  /** for now user is with id 1 **/
    }


    public void postponeBook(String slug) {
        BookEntity bookEntity = bookRepository.findBookEntityBySlug(slug);
        Book2UserTypeEntity book2UserTypeEntity = book2UserTypeRepository.findBook2UserTypeEntityByCodeIs(StatusType.KEPT.toString());
        UserEntity user = userRepository.findById(temporaryUserId).get();  /** for now user is with id 1 **/
        Book2UserEntity book2UserEntity = new Book2UserEntity();
        book2UserEntity.setBook(bookEntity);
        book2UserEntity.setTime(LocalDateTime.now());
        book2UserEntity.setType(book2UserTypeEntity);
        book2UserEntity.setUser(user);
        book2UserRepository.save(book2UserEntity);
    }
}
