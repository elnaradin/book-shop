package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.model.user.UserContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserContactRepository extends JpaRepository<UserContactEntity, Integer> {

    boolean existsByContactAndCodeTimeAfter(String code, LocalDateTime codeTime);
    long deleteByCodeTimeBefore(LocalDateTime codeTime);
    Optional<UserContactEntity> findByContactAndCodeTimeAfter(String formattedValue, LocalDateTime now);

    Optional<UserContactEntity> findTopByContactOrderByCodeTimeDesc(String contact);
}
