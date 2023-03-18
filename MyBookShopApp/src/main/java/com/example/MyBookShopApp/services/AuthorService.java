package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.dto.bookCollections.AuthorWithBooksDto;
import com.example.MyBookShopApp.model.book.BookEntity;
import com.example.MyBookShopApp.model.book.authors.AuthorEntity;
import com.example.MyBookShopApp.repositories.AuthorRepository;
import com.example.MyBookShopApp.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final BookService bookService;


    public Map<String, List<AuthorEntity>> getAuthorsMap() {
        List<AuthorEntity> authors = authorRepository.findAll();
        return authors.stream().sorted(Comparator.comparing(AuthorEntity::getName)).collect(Collectors
                .groupingBy((AuthorEntity a) -> a.getName().substring(0, 1)));
    }

    public AuthorWithBooksDto getAuthorWithBooks(String slug, Integer offset,
                                                 Integer limit) throws NullPointerException {
        Pageable nextPage = PageRequest.of(offset, limit);
        Optional<AuthorEntity> author = authorRepository.findFirstBySlug(slug);
        if (author.isEmpty()) {
            throw new NullPointerException();
        }
        List<BookEntity> bookEntities = bookRepository.findBookEntitiesByAuthorId(author.get().getId(),
                nextPage).getContent();
        Integer booksAmount = bookRepository.countBookEntitiesByAuthorId(author.get().getId());
        return new AuthorWithBooksDto(author.get(), booksAmount, bookService.createBookList(bookEntities));
    }

}
