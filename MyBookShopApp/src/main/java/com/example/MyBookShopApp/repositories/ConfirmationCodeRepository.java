package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.model.user.ConfirmationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ConfirmationCodeRepository extends JpaRepository<ConfirmationCode, Long> {
    boolean existsByValueAndExpiryTimeAfter(String value, LocalDateTime now);
}
