package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.model.jwt.JwtBlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface JwtBlackListRepository extends JpaRepository<JwtBlackList, Integer> {
    boolean existsByJwtAndTimeBefore(String token, LocalDateTime time);
}
