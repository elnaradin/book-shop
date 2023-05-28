package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.dto.ResultDto;
import com.example.MyBookShopApp.errs.UserNotAuthenticatedException;
import com.example.MyBookShopApp.model.book.BookEntity;
import com.example.MyBookShopApp.model.book.links.Book2UserEntity;
import com.example.MyBookShopApp.model.book.links.Book2UserTypeEntity;
import com.example.MyBookShopApp.model.user.UserEntity;
import com.example.MyBookShopApp.repositories.Book2UserRepository;
import com.example.MyBookShopApp.repositories.Book2UserTypeRepository;
import com.example.MyBookShopApp.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class BookStatusService {
    private final BookRepository bookRepository;
    private final Book2UserRepository book2UserRepository;
    private final Book2UserTypeRepository book2UserTypeRepository;
    private final UserService userService;

    public ResultDto changeBookStatus(Integer[] bookIds, String status) throws UserNotAuthenticatedException {
        ResultDto resultDto = new ResultDto();
        UserEntity user = userService.getCurUser();

        List<BookEntity> bookEntitiesByIdIn = bookRepository.findBookEntitiesByIdIn(Arrays.asList(bookIds));
        Book2UserTypeEntity type = book2UserTypeRepository.findBook2UserTypeEntityByCodeIs(status);
        for (BookEntity bookEntity : bookEntitiesByIdIn) {
            if (book2UserRepository.existsByUserIdAndBookId(user.getId(), bookEntity.getId())) {
                book2UserRepository.changeBookStatus(bookEntity.getId(), type, user.getId());
                log.info("old status changed");
            } else {
                Book2UserEntity book2UserEntity = new Book2UserEntity();
                book2UserEntity.setBook(bookEntity);
                book2UserEntity.setTime(LocalDateTime.now());
                book2UserEntity.setType(type);
                book2UserEntity.setUser(user);
                book2UserRepository.save(book2UserEntity);
                log.info("status created");
            }
            log.info("status of " + bookEntity.getTitle() + " now is  " + status);
        }
        resultDto.setResult(true);
        return resultDto;
    }

}
