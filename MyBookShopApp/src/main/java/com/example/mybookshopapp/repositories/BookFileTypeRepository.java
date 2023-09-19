package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.model.book.file.BookFileTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface BookFileTypeRepository extends JpaRepository<BookFileTypeEntity, Integer> {
    BookFileTypeEntity findByName(String name);
}