package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.dto.review.ReviewDto;
import com.example.mybookshopapp.model.book.BookEntity;
import com.example.mybookshopapp.model.book.review.BookReviewEntity;
import com.example.mybookshopapp.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<BookReviewEntity, Integer> {
    long deleteByHash(Object unknownAttr1);
    Optional<BookReviewEntity> findByHash(Object unknownAttr1);
    @Transactional
    @Modifying
    @Query("update BookReviewEntity b set b.text = ?1, b.time = ?2 where b.book = ?3 and b.user = ?4")
    int updateTextAndTimeByBookAndUser(String text, LocalDateTime time, BookEntity book, UserEntity user);

    boolean existsByBookAndUser(BookEntity book, UserEntity user);

    @Query(value = "select * from (select   " +
            "    br.hash hash,   " +
            "    u.name userName,   " +
            "       br.time time,   " +
            "       br.text text,   " +
            "       case when r.value is null then 0 else r.value end  rating,   " +
            "       sum(case when brl.value = 1 then 1 else 0 end)  likeCount,   " +
            "       sum(case when brl.value = -1 then 1 else 0 end) dislikeCount   " +
            " from book_review br   " +
            "         left join book_review_like brl on br.id = brl.review_id   " +
            "         join books b on b.id = br.book_id   " +
            "         join users u on br.user_id = u.id   " +
            "         left join ratings r on (u.id = r.user_id and r.book_id = b.id)    " +
            " group by b.slug, br.hash, br.time, br.text, u.name, r.value " +
            " having b.slug like :slug) as reviews_table " +
            " order by likeCount - dislikeCount desc",
            nativeQuery = true)
    List<ReviewDto> getReviewListBySlug(@Param("slug") String slug);

    @Query(value = "select * from (select   " +
            "    br.hash hash,   " +
            "    u.name userName,   " +
            "       br.time time,   " +
            "       br.text text,   " +
            "       case when r.value is null then 0 else r.value end  rating,   " +
            "       sum(case when brl.value = 1 then 1 else 0 end)  likeCount,   " +
            "       sum(case when brl.value = -1 then 1 else 0 end) dislikeCount," +
            "       (select case when brl2.value = 1 then true else false end " +
            "       from book_review_like brl2 " +
            "       left join users u2 on u2.id = brl2.user_id " +
            "       where u2.email = :email and brl2.review_id = br.id) isLiked, " +
            "       (select case when brl2.value = -1 then true else false end " +
            "       from book_review_like brl2 " +
            "       left join users u2 on u2.id = brl2.user_id " +
            "       where u2.email = :email and brl2.review_id = br.id) isDisliked " +
            " from book_review br   " +
            "         left join book_review_like brl on br.id = brl.review_id   " +
            "         join books b on b.id = br.book_id   " +
            "         join users u on br.user_id = u.id   " +
            "         left join ratings r on (u.id = r.user_id and r.book_id = b.id)   " +
            " group by b.slug, br.hash, br.id, br.time, br.text, u.name, r.value   " +
            " having b.slug like :slug) as reviews_table " +
            " order by likeCount - dislikeCount desc",
            nativeQuery = true)
    List<ReviewDto> getReviewListBySlugAndEmail(@Param("slug") String slug, @Param("email") String email);
}
