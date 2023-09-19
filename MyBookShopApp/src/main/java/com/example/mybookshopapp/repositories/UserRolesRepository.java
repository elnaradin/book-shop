package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.model.user.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRolesRepository extends JpaRepository<UserRoleEntity, Integer> {
    UserRoleEntity findByRole(String role);
}