package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.model.book.review.BookReviewEntity;
import com.example.mybookshopapp.model.book.review.BookReviewLikeEntity;
import com.example.mybookshopapp.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ReviewLikeRepository extends JpaRepository<BookReviewLikeEntity, Integer> {
    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update BookReviewLikeEntity b set b.time = ?1, b.value = ?2 where b.user = ?3 and b.review = ?4")
    int updateTimeAndValueByUserAndReview(LocalDateTime time, short value, UserEntity user, BookReviewEntity review);

    boolean existsByReviewAndUser(BookReviewEntity review, UserEntity user);


    Optional<BookReviewLikeEntity> findByReviewIdAndUser(Integer reviewId, UserEntity user);
}
