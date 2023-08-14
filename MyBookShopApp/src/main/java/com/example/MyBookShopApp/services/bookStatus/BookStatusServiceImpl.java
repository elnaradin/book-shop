package com.example.MyBookShopApp.services.bookStatus;

import com.example.MyBookShopApp.dto.ResultDto;
import com.example.MyBookShopApp.dto.book.BookSlugs;
import com.example.MyBookShopApp.dto.book.BooksPageDto;
import com.example.MyBookShopApp.dto.book.ChangeStatusPayload;
import com.example.MyBookShopApp.dto.book.ShortBookDto;
import com.example.MyBookShopApp.model.book.BookEntity;
import com.example.MyBookShopApp.model.book.links.Book2UserEntity;
import com.example.MyBookShopApp.model.book.links.Book2UserTypeEntity;
import com.example.MyBookShopApp.model.enums.StatusType;
import com.example.MyBookShopApp.model.user.UserEntity;
import com.example.MyBookShopApp.repositories.Book2UserRepository;
import com.example.MyBookShopApp.repositories.Book2UserTypeRepository;
import com.example.MyBookShopApp.repositories.BookRepository;
import com.example.MyBookShopApp.repositories.UserRepository;
import com.example.MyBookShopApp.services.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class BookStatusServiceImpl implements BookStatusService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final Book2UserRepository book2UserRepository;
    private final Book2UserTypeRepository book2UserTypeRepository;
    private final CookieUtils cookieUtils;

    @Override
    public BooksPageDto getAnonymUserBooks(StatusType status) {
        List<String> slugs = cookieUtils.getBookSlugsByStatus(status);
        if (CollectionUtils.isEmpty(slugs)) {
            return new BooksPageDto();
        }
        List<ShortBookDto> books = bookRepository.getBooksBySlugsIn(slugs);
        return BooksPageDto.builder()
                .books(books)
                .totalPrice(books.stream()
                        .map(ShortBookDto::getPrice)
                        .mapToInt(Integer::intValue)
                        .sum())
                .totalDiscountPrice(books.stream()
                        .map(ShortBookDto::getDiscountPrice)
                        .mapToInt(Integer::intValue)
                        .sum())
                .slugs(books.stream()
                        .map(ShortBookDto::getSlug)
                        .collect(Collectors.toList()))
                .build();

    }

    @Override
    @Transactional
    public ResultDto changeBookStatus(ChangeStatusPayload payload, String userEmail) {
        List<BookEntity> books = bookRepository.findBookEntitiesBySlugIn(payload.getBookIds());
        UserEntity userEntity = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        ResultDto resultDto = ResultDto.builder().result(true).build();
        if (payload.getStatus().equals(StatusType.UNLINK)) {
            for (BookEntity book : books) {
                book2UserRepository.deleteByBookAndUser(book, userEntity);
            }
            return resultDto;
        }
        Book2UserTypeEntity code = book2UserTypeRepository.findByCode(payload.getStatus());
        for (BookEntity book : books) {
            if (book2UserRepository.existsByUserAndBook(userEntity, book)) {
                book2UserRepository.updateTypeByUserAndBook(code, userEntity, book);
            } else {
                Book2UserEntity link = Book2UserEntity.builder()
                        .book(book)
                        .user(userEntity)
                        .type(code)
                        .time(LocalDateTime.now())
                        .build();
                book2UserRepository.saveAndFlush(link);
            }
        }
        return resultDto;
    }

    @Override
    public BooksPageDto getBooksByStatus(StatusType status, Authentication authentication) {
        List<ShortBookDto> books = bookRepository.getBooksByUserAndStatus(
                authentication.getName(), status.toString());
        return BooksPageDto.builder()
                .books(books)
                .totalPrice(books.stream()
                        .map(ShortBookDto::getPrice)
                        .mapToInt(Integer::intValue)
                        .sum())
                .totalDiscountPrice(books.stream()
                        .map(ShortBookDto::getDiscountPrice)
                        .mapToInt(Integer::intValue)
                        .sum())
                .slugs(books.stream()
                        .map(ShortBookDto::getSlug)
                        .collect(Collectors.toList()))
                .build();

    }

    @Override
    public StatusType getBookStatus(String slug, String email) {
        return book2UserRepository.getCodeByBookSlugAndEmail(slug, email);
    }

    @Override
    public Map<String, List<String>> getUserBookSlugs(Authentication authentication) {
        Map<String, List<String>> slugsByStatus = new HashMap<>();
        if (authentication == null) {
            return getSlugsFromCookies();
        }
        for (StatusType statusType :
                StatusType.values()) {
            List<String> slugs = bookRepository
                    .findBookSlugsByUserEmail(authentication.getName()).stream()
                    .filter(bookSlugs -> bookSlugs.getStatus().equals(statusType))
                    .map(BookSlugs::getSlug)
                    .collect(Collectors.toList());
            slugsByStatus.put(statusType.toString(), slugs);
        }
        return slugsByStatus;
    }

    private Map<String, List<String>> getSlugsFromCookies() {
        Map<String, List<String>> slugsMap = new HashMap<>();
        slugsMap.put(StatusType.CART.toString(), cookieUtils.getSlugsFromCookie(StatusType.CART));
        slugsMap.put(StatusType.KEPT.toString(), cookieUtils.getSlugsFromCookie(StatusType.KEPT));
        return slugsMap;
    }

}
