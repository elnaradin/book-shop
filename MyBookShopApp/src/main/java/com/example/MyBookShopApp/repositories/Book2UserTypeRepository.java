package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.model.book.links.Book2UserTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Book2UserTypeRepository extends JpaRepository<Book2UserTypeEntity, Integer> {
    Book2UserTypeEntity findBook2UserTypeEntityByCodeIs(String code);
}
