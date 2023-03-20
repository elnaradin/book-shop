package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.model.book.RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<RatingEntity, Integer> {
    @Query("select r from RatingEntity r " +
            "join Book2RatingEntity br on r.id=br.rating.id " +
            "join BookEntity b on br.book.id = b.id " +
            "where r.user.id =?1 and b.id =?2")
    Optional<RatingEntity> findRatingEntityByUserIdAndBookId(Integer userId, Integer bookId);

    @Query("select sum(r.value)/count(r) from RatingEntity r " +
            "join Book2RatingEntity br on r.id= br.rating.id " +
            "join BookEntity b on br.book.id = b.id " +
            "where b.id = ?1")
    Integer getBookRating(Integer bookId);

    @Query("select count(r) from RatingEntity r " +
            "join Book2RatingEntity br on r.id=br.rating.id " +
            "join BookEntity b on br.book.id=b.id " +
            "where b.slug = ?1")
    Integer getBookRatingAmount(String bookSlug);

    @Query("select count(r) from RatingEntity r  " +
            "join Book2RatingEntity br on r.id=br.rating.id " +
            "join BookEntity b on br.book.id=b.id " +
            "where r.value =?1 and b.slug = ?2")
    Integer getBookRatingAmountByStars(int starNumber, String bookSlug);

}
