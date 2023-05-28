package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.model.genre.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<GenreEntity, Integer> {
    Optional<GenreEntity> findGenreEntitiesBySlugIs(String slug);

    @Query("select g from GenreEntity g where g.parent = null")
    List<GenreEntity> findParentGenreEntities();

}
