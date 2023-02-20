package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.model.book.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BookRepository extends JpaRepository<BookEntity, Integer> {

}
