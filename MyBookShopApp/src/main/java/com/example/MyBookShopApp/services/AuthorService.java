package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.repositories.AuthorRepository;
import com.example.MyBookShopApp.model.book.AuthorEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Map<String, List<AuthorEntity>> getAuthorsMap() {
        List<AuthorEntity> authors = authorRepository.findAll();
        return authors.stream().collect(Collectors
                .groupingBy((AuthorEntity a) -> a.getName().substring(0,1)));
    }
}
