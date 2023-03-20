package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.model.book.review.BookReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<BookReviewEntity, Integer> {

    List<BookReviewEntity> findBookReviewEntitiesByBookSlug(String slug);
}
