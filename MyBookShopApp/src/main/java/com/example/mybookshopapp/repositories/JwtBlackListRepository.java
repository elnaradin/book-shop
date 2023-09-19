package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.model.jwt.JwtBlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtBlackListRepository extends JpaRepository<JwtBlackList, Integer> {
    boolean existsByJwt(String token);
}
