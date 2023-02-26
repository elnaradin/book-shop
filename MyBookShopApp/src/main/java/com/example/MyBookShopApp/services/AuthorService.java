package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.dto.AuthorWithBooksDto;
import com.example.MyBookShopApp.repositories.AuthorRepository;
import com.example.MyBookShopApp.model.book.authors.AuthorEntity;
import com.example.MyBookShopApp.repositories.BookRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorService(AuthorRepository authorRepository,
                         BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public Map<String, List<AuthorEntity>> getAuthorsMap() {
        List<AuthorEntity> authors = authorRepository.findAll();
        return authors.stream().collect(Collectors
                .groupingBy((AuthorEntity a) -> a.getName().substring(0,1)));
    }

    public AuthorWithBooksDto getAuthorWithBooks(String slug, Integer offset,
                                                 Integer limit) throws NullPointerException{
        Pageable nextPage = PageRequest.of(offset, limit);
        Optional<AuthorEntity> author = authorRepository.findFirstBySlug(slug);
        if(author.isEmpty()){
            throw new NullPointerException();
        }
        return new AuthorWithBooksDto(author.get(),
                bookRepository.findBookEntitiesByAuthorsIn(author.get().getBooks(),
                nextPage).getContent());
    }

}
