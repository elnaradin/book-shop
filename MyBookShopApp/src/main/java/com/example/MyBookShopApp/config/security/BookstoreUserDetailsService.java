package com.example.MyBookShopApp.config.security;

import com.example.MyBookShopApp.model.user.UserEntity;
import com.example.MyBookShopApp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        String phoneRegex = "^\\+7 \\([0-9]{3}\\) [0-9]{3}-[0-9]{2}-[0-9]{2}$";
        if (bookstoreUser.isEmpty() && name.matches(phoneRegex)) {
            bookstoreUser = bookstoreUserRepository
                    .findFirstByPhone(name);
        }
        return new BookstoreEmailUserDetails(bookstoreUser
                .orElseThrow(() -> new UsernameNotFoundException("User not found")));
    }
}
