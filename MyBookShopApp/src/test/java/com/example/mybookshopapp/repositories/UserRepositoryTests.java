package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.model.user.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
@SpringBootTest
@TestPropertySource("/application-test.properties")
class UserRepositoryTests {
    private final UserRepository userRepository;

    @Autowired
    public UserRepositoryTests(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    void testAddNewUser(){
        UserEntity user = new UserEntity();
        user.setEmail("user@mail.com");
        user.setPhone("+79898989898");
        user.setPassword("123123123");
        user.setFullName("User Name");
        assertNotNull(userRepository.save(user));
    }

}