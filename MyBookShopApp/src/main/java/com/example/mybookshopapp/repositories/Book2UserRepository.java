package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.model.book.BookEntity;
import com.example.mybookshopapp.model.book.links.Book2UserEntity;
import com.example.mybookshopapp.model.book.links.Book2UserTypeEntity;
import com.example.mybookshopapp.model.enums.StatusType;
import com.example.mybookshopapp.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;


@Repository
public interface Book2UserRepository extends JpaRepository<Book2UserEntity, Integer> {
    Optional<Book2UserEntity> findByBookAndUser(BookEntity book, UserEntity user);
    void deleteByTypeCodeAndTimeBefore(StatusType code, LocalDateTime time);
    long deleteByBookAndUser(BookEntity book, UserEntity user);

    @Transactional
    @Modifying
    @Query("update Book2UserEntity b set b.type = ?1 where b.user = ?2 and b.book = ?3")
    void updateTypeByUserAndBook(Book2UserTypeEntity type, UserEntity user, BookEntity book);

    boolean existsByUserAndBook(UserEntity user, BookEntity book);

    @Query(value = "select coalesce((select b2ut.code code  " +
            "                 from book2user b2u  " +
            "                          join users u on u.id = b2u.user_id  " +
            "                          join books b on b.id = b2u.book_id  " +
            "                          join book2user_type b2ut on b2ut.id = b2u.type_id  " +
            "                 where b.slug like :slug  " +
            "                   and u.email like :email), 'UNLINK')", nativeQuery = true)
    String getCodeByBookSlugAndEmail(
            @Param("slug") String slug,
            @Param("email") String email
    );
}
