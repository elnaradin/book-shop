package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.model.genre.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<GenreEntity, Integer> {
    Optional<GenreEntity> findGenreEntitiesBySlugIs(String slug);
}
