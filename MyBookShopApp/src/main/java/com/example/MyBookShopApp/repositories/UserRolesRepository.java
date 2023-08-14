package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.model.user.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRolesRepository extends JpaRepository<UserRoleEntity, Integer> {
    UserRoleEntity findByRole(String role);
}