package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.dto.book.BookDto;
import com.example.MyBookShopApp.model.book.BookEntity;
import com.example.MyBookShopApp.model.book.links.Book2UserEntity;
import com.example.MyBookShopApp.model.book.links.Book2UserTypeEntity;
import com.example.MyBookShopApp.model.user.UserEntity;
import com.example.MyBookShopApp.repositories.Book2UserRepository;
import com.example.MyBookShopApp.repositories.Book2UserTypeRepository;
import com.example.MyBookShopApp.repositories.BookRepository;
import com.example.MyBookShopApp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    public List<BookDto> getBooksByStatus(String status) {
        List<BookEntity> bookEntities = bookRepository
                .findBookEntitiesByUserAndStatus(temporaryUserId, /** for now user is with id 1 **/
                        status);
        return bookService.createBookList(bookEntities);
    }


//    public void removeBook(String slug, String status) {
//        book2UserRepository.deleteBook2UserEntityByBookSlug(slug, status);
//    }

    public boolean booksByStatusExist(String status) {
        return book2UserRepository.existsByTypeCodeAndUserId(status, temporaryUserId);  /** for now user is with id 1 **/
    }


    public void changeBookStatus(int[] bookIds, String status) {
        List<BookEntity> bookEntitiesByIdIn = bookRepository.findBookEntitiesByIdIn(bookIds);
        UserEntity user = userRepository.findById(temporaryUserId).get();
        Book2UserTypeEntity book2UserTypeEntity = book2UserTypeRepository.findBook2UserTypeEntityByCodeIs(status);
        for (BookEntity bookEntity : bookEntitiesByIdIn) {
            if (book2UserRepository.existsByUserIdAndBookId(
                    user.getId(),
                    bookEntity.getId())) {
                book2UserRepository.changeBookStatus(bookEntity.getId(),
                        book2UserTypeRepository.findBook2UserTypeEntityByCodeIs(status).getId(),
                        temporaryUserId);/** for now user is with id 1 **/

            } else {
                Book2UserEntity book2UserEntity = new Book2UserEntity();
                book2UserEntity.setBook(bookEntity);
                book2UserEntity.setTime(LocalDateTime.now());
                book2UserEntity.setType(book2UserTypeEntity);
                book2UserEntity.setUser(user);
                book2UserRepository.save(book2UserEntity);
            }
        }


    }
}
