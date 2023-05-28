package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.model.book.authors.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, Integer> {

    Optional<AuthorEntity> findFirstBySlug(String slug);

}
