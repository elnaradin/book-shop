package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.model.book.authors.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, Integer> {

    Optional<AuthorEntity> findFirstBySlug(String slug);

    @Query(value = "SELECT a from AuthorEntity a " +
            "JOIN Book2AuthorEntity ba on a.id = ba.author.id " +
            "JOIN BookEntity b on ba.book.id = b.id " +
            "where b.id=?1")
    List<AuthorEntity> findAuthorsByBookEntity(Integer bookId);
}
