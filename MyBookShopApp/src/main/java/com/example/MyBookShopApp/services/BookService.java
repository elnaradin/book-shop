package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.repositories.BookRepository;
import com.example.MyBookShopApp.model.book.BookEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookEntity> getBooksData() {
        return bookRepository.findAll();
    }
}
