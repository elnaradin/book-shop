package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.dto.file.BookFileDto;
import com.example.mybookshopapp.model.book.BookEntity;
import com.example.mybookshopapp.model.book.file.BookFileEntity;
import com.example.mybookshopapp.model.book.file.BookFileTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookFileRepository extends JpaRepository<BookFileEntity, Integer> {
    boolean existsByBookAndType(BookEntity book, BookFileTypeEntity type);
    long deleteByBookAndType(BookEntity book, BookFileTypeEntity type);
    long deleteByPath(String path);
    BookFileEntity findFirstByHash(String hash);

    @Query("select new com.example.mybookshopapp.dto.file.BookFileDto(f.hash, f.type.name, f.type.description) from BookFileEntity f " +
            "where f.book.slug like ?1")
    List<BookFileDto> findFilesByBookSlug(String slug);
}
