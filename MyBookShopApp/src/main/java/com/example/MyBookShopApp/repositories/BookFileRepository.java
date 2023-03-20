package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.model.book.file.BookFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookFileRepository extends JpaRepository<BookFileEntity, Integer> {
    BookFileEntity findFirstByHash(String hash);

    List<BookFileEntity> findFilesByBookSlug(String slug);
}
