package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.dto.book.ShortBookDtoProjection;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource("/application-test.properties")
@Slf4j
class BookRepositoryTests {

    private final BookRepository bookRepository;

    @Autowired
    public BookRepositoryTests(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Test
    void findBookEntitiesByTitleContainingIgnoreCase() {
        String token = "night";
        Page<ShortBookDtoProjection> bookEntitiesByTitleContainingIgnoreCase
                = bookRepository.findBooksByTitleContaining(token, Pageable.unpaged());

        assertNotNull(bookEntitiesByTitleContainingIgnoreCase);
        assertFalse(bookEntitiesByTitleContainingIgnoreCase.isEmpty());
        for (ShortBookDtoProjection bookEntity : bookEntitiesByTitleContainingIgnoreCase) {
            log.info(bookEntity.getTitle());
            assertThat(bookEntity.getTitle()).containsIgnoringCase(token);
        }

    }

    @Test
    void findPopularBooks() {
        Page<ShortBookDtoProjection> popularBooks = bookRepository.getPopularBooks(List.of(""), PageRequest.of(1, 10));
        assertNotNull(popularBooks);
        assertFalse(popularBooks.isEmpty());
        assertThat(popularBooks.getSize()).isGreaterThan(1);
        log.info(popularBooks.getContent().toString());

    }

    @Test
    void findBookEntitiesByAuthorId() {
        String token = "author-uta-899";
        Page<ShortBookDtoProjection> books = bookRepository.getBooksListByAuthor(token, Pageable.unpaged());
        assertNotNull(books);
        assertFalse(books.isEmpty());
    }


}