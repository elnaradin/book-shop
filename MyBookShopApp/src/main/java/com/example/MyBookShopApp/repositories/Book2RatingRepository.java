package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.model.book.links.Book2RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Book2RatingRepository extends JpaRepository<Book2RatingEntity, Integer> {
}
