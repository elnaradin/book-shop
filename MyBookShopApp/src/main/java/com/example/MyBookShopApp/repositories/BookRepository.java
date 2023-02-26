package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.model.book.BookEntity;
import com.example.MyBookShopApp.model.book.links.Book2AuthorEntity;
import com.example.MyBookShopApp.model.book.links.Book2GenreEntity;
import com.example.MyBookShopApp.model.book.links.Book2TagEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Set;


@Repository
public interface BookRepository extends JpaRepository<BookEntity, Integer> {

    Page<BookEntity> findBookEntitiesByTitleContaining(String title, Pageable nextPage);

    Page<BookEntity> findBookEntitiesByPubDateBetweenOrderByPubDateDesc(Date from, Date to, Pageable nextPage);

    Page<BookEntity> findBookEntitiesByTagsIn(Set<Book2TagEntity> tags, Pageable pageable);

    Page<BookEntity> findBookEntitiesByGenresIn (Set<Book2GenreEntity> genres, Pageable pageable);

    Page<BookEntity> findBookEntitiesByAuthorsIn(Set<Book2AuthorEntity> authors, Pageable pageable);

}
