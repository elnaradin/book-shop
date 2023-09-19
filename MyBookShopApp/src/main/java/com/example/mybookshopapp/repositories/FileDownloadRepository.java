package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.model.book.BookEntity;
import com.example.mybookshopapp.model.book.file.FileDownloadEntity;
import com.example.mybookshopapp.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileDownloadRepository extends JpaRepository<FileDownloadEntity, Integer> {
    Optional<FileDownloadEntity> findByUserAndBook(UserEntity user, BookEntity book);

}
