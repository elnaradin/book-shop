package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.model.book.links.Book2UserEntity;
import com.example.MyBookShopApp.model.book.links.Book2UserTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface Book2UserRepository extends JpaRepository<Book2UserEntity, Integer> {

    boolean existsByUserIdAndBookId(Integer userId, Integer bookId);

    @Transactional
    @Modifying
    @Query("update Book2UserEntity bu set bu.type = ?2 where bu.book.id = ?1 and bu.user.id = ?3")
    void changeBookStatus(Integer bookId, Book2UserTypeEntity book2UserType, Integer userId);

}
