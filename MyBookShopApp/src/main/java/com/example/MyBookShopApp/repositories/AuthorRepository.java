package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.dto.author.FullAuthorDto;
import com.example.MyBookShopApp.dto.author.ShortAuthorDto;
import com.example.MyBookShopApp.model.book.authors.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, Integer> {

    @Query("select new com.example.MyBookShopApp.dto.author.FullAuthorDto(a.slug," +
            " a.name, a.photo, a.description) " +
            "from AuthorEntity a " +
            "where a.slug = ?1")
    Optional<FullAuthorDto> getFullAuthorDtoBySlug(String slug);

    @Query("select new com.example.MyBookShopApp.dto.author.ShortAuthorDto(a.slug, a.name) " +
            "from AuthorEntity a")
    List<ShortAuthorDto> findAllAuthors();

    @Query("select new com.example.MyBookShopApp.dto.author.ShortAuthorDto(a.slug, a.name) " +
            "from AuthorEntity a " +
            "where a.slug = ?1")
    Optional<ShortAuthorDto> getShortAuthorDtoBySlug(String slug);

    @Query("select new com.example.MyBookShopApp.dto.author.ShortAuthorDto(b2a.author.slug, b2a.author.name) " +
            "from Book2AuthorEntity b2a " +
            "where b2a.book.slug like  ?1 order by b2a.sortIndex")
    List<ShortAuthorDto> getAuthorsByBookSlug(String slug);
}
