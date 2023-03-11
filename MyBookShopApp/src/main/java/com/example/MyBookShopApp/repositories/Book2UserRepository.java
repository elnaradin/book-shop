package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.model.book.links.Book2UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface Book2UserRepository extends JpaRepository<Book2UserEntity, Integer> {

    void deleteBook2UserEntityByBookSlug(String slug);

    boolean existsByTypeCodeAndUserId(String code, Integer userId);


}
