package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.model.book.BookEntity;
import com.example.mybookshopapp.model.book.authors.AuthorEntity;
import com.example.mybookshopapp.model.book.links.Book2AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Book2AuthorRepository extends JpaRepository<Book2AuthorEntity, Integer> {

    int deleteByBook(BookEntity book);

    boolean existsByBookAndAuthor(BookEntity book, AuthorEntity author);
}