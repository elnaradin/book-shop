package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.dto.genre.GenreDto;
import com.example.MyBookShopApp.dto.genre.ShortGenreDto;
import com.example.MyBookShopApp.model.genre.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<GenreEntity, Integer> {

    @Query("select new com.example.MyBookShopApp.dto.genre.GenreDto(g.id, g.slug, g.name, g.parent.id, " +
            "count(distinct b.id)) from GenreEntity g " +
            "join Book2GenreEntity bg on bg.genre.id = g.id " +
            "join BookEntity b on bg.book.id = b.id " +
            "group by g.id, g.slug, g.name, g.parent.id " +
            "order by g.name")
    List<GenreDto> getAllGenres();

    @Query("select new com.example.MyBookShopApp.dto.genre.ShortGenreDto(g.slug, g.name) from GenreEntity g " +
            "where g.slug like ?1")
    Optional<ShortGenreDto> getShortGenreDto(String genreSlug);
}
