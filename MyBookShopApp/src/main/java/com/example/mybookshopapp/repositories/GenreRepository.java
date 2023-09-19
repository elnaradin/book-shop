package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.dto.genre.GenreDto;
import com.example.mybookshopapp.dto.genre.ShortGenreDto;
import com.example.mybookshopapp.model.genre.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<GenreEntity, Integer> {
    long deleteBySlug(String slug);
    Optional<GenreEntity> findBySlug(String slug);
    List<GenreEntity> findBySlugIn(Collection<String> slugs);

    @Query("select new com.example.mybookshopapp.dto.genre.GenreDto(g.id, g.slug, g.name, g.parent.id, " +
            "count(distinct b.id)) from GenreEntity g " +
            "left join Book2GenreEntity bg on bg.genre.id = g.id " +
            "left join BookEntity b on bg.book.id = b.id " +
            "group by g.id, g.slug, g.name, g.parent.id " +
            "order by g.name")
    List<GenreDto> getAllGenres();

    @Query("select new com.example.mybookshopapp.dto.genre.ShortGenreDto(g.slug, g.name) from GenreEntity g " +
            "where g.slug like ?1")
    Optional<ShortGenreDto> getShortGenreDto(String genreSlug);
    @Query("select new com.example.mybookshopapp.dto.genre.ShortGenreDto(g.slug, g.name) from GenreEntity g " +
            "order by g.name asc ")
    List<ShortGenreDto> getShortGenreDtos();

    @Query("select new com.example.mybookshopapp.dto.genre.ShortGenreDto(b2g.genre.slug, b2g.genre.name) from Book2GenreEntity b2g " +
            "where b2g.book.slug like ?1")
    List<ShortGenreDto> getShortGenreDtosByBookSlug(String slug);
}
