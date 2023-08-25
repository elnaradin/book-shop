package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findFirstByPhone(String phone);

    boolean existsByEmail(String s);

    boolean existsByPhone(String s);

    Optional<UserEntity> findByEmail(String email);
}
