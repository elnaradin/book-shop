package com.example.MyBookShopApp.config.security;

import com.example.MyBookShopApp.model.user.UserEntity;
import com.example.MyBookShopApp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookstoreUserDetailsService implements UserDetailsService {
    private final UserRepository bookstoreUserRepository;

    @Override
    public UserDetails loadUserByUsername(String name) {
        Optional<UserEntity> bookstoreUser = bookstoreUserRepository
                .findByEmail(name);
        String formattedPhone = formatPhone(name);
        if (bookstoreUser.isEmpty()) {
            bookstoreUser = bookstoreUserRepository
                    .findFirstByPhone(formattedPhone);
        }
        return bookstoreUser.orElseThrow();
    }

    private String formatPhone(String phone) {
        String digits = phone.replaceAll("[^\\\\d.]", "");
        if (digits.length() == 11) {
            if (digits.startsWith("8")) {
                digits = digits.replaceFirst("8", "7");
            }
            return "+" + digits;
        }
        if (digits.length() == 10) {
            return "+7" + digits;
        }
        return digits;
    }
}
