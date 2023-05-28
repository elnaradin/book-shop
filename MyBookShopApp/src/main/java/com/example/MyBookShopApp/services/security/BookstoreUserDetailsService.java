package com.example.MyBookShopApp.services.security;

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
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        log.info("username: " + s);
        Optional<UserEntity> bookstoreUser = bookstoreUserRepository.findBookstoreUserByEmail(s);
        if (bookstoreUser.isPresent()) {
            return new BookstoreUserDetails(bookstoreUser.get());
        } else {
            throw new UsernameNotFoundException("user not found");
        }
    }
}
