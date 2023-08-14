package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.model.book.BookEntity;
import com.example.MyBookShopApp.model.book.RatingEntity;
import com.example.MyBookShopApp.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RatingRepository extends JpaRepository<RatingEntity, Integer> {
    @Transactional
    @Modifying
    @Query("update RatingEntity r set r.value = ?1 where r.user = ?2 and r.book = ?3")
    void updateValueByUserAndBook(Integer value, UserEntity user, BookEntity book);

    boolean existsByUserAndBook(UserEntity user, BookEntity book);

}
