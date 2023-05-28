package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.model.user.User2RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface User2RoleRepository extends JpaRepository<User2RoleEntity, Integer> {
}