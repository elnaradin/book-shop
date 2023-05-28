package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.dto.HeaderValues;
import com.example.MyBookShopApp.dto.book.BooksWithTotalPrice;
import com.example.MyBookShopApp.dto.mappers.BooksMapper;
import com.example.MyBookShopApp.errs.UserNotAuthenticatedException;
import com.example.MyBookShopApp.model.enums.StatusType;
import com.example.MyBookShopApp.model.enums.UserRoles;
import com.example.MyBookShopApp.model.user.User2RoleEntity;
import com.example.MyBookShopApp.model.user.UserEntity;
import com.example.MyBookShopApp.model.user.UserRoleEntity;
import com.example.MyBookShopApp.repositories.BookRepository;
import com.example.MyBookShopApp.repositories.User2RoleRepository;
import com.example.MyBookShopApp.repositories.UserRepository;
import com.example.MyBookShopApp.repositories.UserRolesRepository;
import com.example.MyBookShopApp.services.security.BookstoreUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRolesRepository userRolesRepository;
    private final User2RoleRepository user2RoleRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BooksMapper booksMapper;

    public void showListOfBooksSelectedByUser(StatusType statusType,
                                              Model model)  {


        BooksWithTotalPrice booksWithTotalPrice = booksMapper.toBooksWithTotalPrice(bookRepository
                .getBooksByUserIdAndStatus(getCurUser().getId(), statusType.toString()));
        model.addAttribute("selectedBooks", booksWithTotalPrice);
    }

    public UserEntity getCurUser() {
        UserEntity user;
        try {
            user = ((BookstoreUserDetails) SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getPrincipal())
                    .getBookstoreUser();
        } catch (NullPointerException e) {
            throw new UserNotAuthenticatedException("No user currently in context");
        }
        return user;
    }

    public boolean checkUserRoles(UserRoles role) {
        BookstoreUserDetails principal = (BookstoreUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return principal.getAuthorities()
                .stream()
                .anyMatch(authority -> authority.getAuthority().contains(role.toString()));
    }

    public HeaderValues findHeaderValues() {
        HeaderValues headerValues = new HeaderValues();
        UserEntity user = getCurUser();
        Integer cart = bookRepository.countBooksByUserIdAndStatus(user.getId(), "CART");
        Integer postponed = bookRepository.countBooksByUserIdAndStatus(user.getId(), "KEPT");
        Integer balance = userRepository.getBalanceById(user.getId());
        Integer my = bookRepository.countBooksByUserIdAndStatus(user.getId(), "PAID");
        headerValues.setIsAuthorized(checkUserRoles(UserRoles.USER));
        headerValues.setMy(my);
        headerValues.setBalance(balance);
        headerValues.setUsername(user.getName());
        headerValues.setCart(cart);
        headerValues.setPostponed(postponed);
        log.info(headerValues.toString());
        return headerValues;
    }

    public UserEntity createEmptyUser() {
        UserEntity emptyUser = new UserEntity();
        String identifier = UUID.randomUUID().toString();
        emptyUser.setEmail(identifier);
        emptyUser.setRegTime(LocalDateTime.now());
        userRepository.save(emptyUser);
        UserRoleEntity userRoleEntity = userRolesRepository
                .findById(UserRoles.ANONYMOUS.getId()).get();
        User2RoleEntity user2Role = new User2RoleEntity();
        user2Role.setRole(userRoleEntity);
        user2Role.setUser(emptyUser);
        user2RoleRepository.save(user2Role);
        return emptyUser;
    }


    @Transactional
    @Scheduled(fixedDelay = 600000)
    public void cleanFromEmptyUsers() {
        log.info("deleting empty users");
        userRepository.deleteEmptyUsers(LocalDateTime.now().minusMonths(1));
    }


    public String getCurUserRoles() {
        BookstoreUserDetails principal = (BookstoreUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        StringJoiner sj = new StringJoiner(", ");
        principal.getAuthorities().forEach(a -> sj.add(a.getAuthority()));
        return sj.toString();
    }
}
