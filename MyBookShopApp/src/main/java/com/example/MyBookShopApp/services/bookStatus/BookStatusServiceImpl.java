package com.example.MyBookShopApp.services.bookStatus;

import com.example.MyBookShopApp.config.security.IAuthenticationFacade;
import com.example.MyBookShopApp.dto.ResultDto;
import com.example.MyBookShopApp.dto.book.BookSlugs;
import com.example.MyBookShopApp.dto.book.BooksPageDto;
import com.example.MyBookShopApp.dto.book.ChangeStatusPayload;
import com.example.MyBookShopApp.dto.book.ShortBookDtoProjection;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
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
    private final IAuthenticationFacade facade;

    @Override
    public BooksPageDto getAnonymUserBooks(StatusType status) {
        List<String> slugs = cookieUtils.getBookSlugsByStatus(status);
        if (CollectionUtils.isEmpty(slugs)) {
            return new BooksPageDto();
        }
        List<ShortBookDtoProjection> books = bookRepository.getBooksBySlugsIn(slugs);
        return BooksPageDto.builder()
                .books(books)
                .totalPrice(books.stream()
                        .map(ShortBookDtoProjection::getPrice)
                        .mapToInt(Integer::intValue)
                        .sum())
                .totalDiscountPrice(books.stream()
                        .map(ShortBookDtoProjection::getDiscountPrice)
                        .mapToInt(Integer::intValue)
                        .sum())
                .slugs(books.stream()
                        .map(ShortBookDtoProjection::getSlug)
                        .collect(Collectors.toList()))
                .build();

    }

    @Override
    @Transactional
    public ResultDto changeBookStatus(ChangeStatusPayload payload) {
        List<BookEntity> books = bookRepository.findBookEntitiesBySlugIn(payload.getBookIds());
        UserEntity userEntity = facade.getPrincipal();
        ResultDto resultDto = ResultDto.builder().result(true).build();
        if (payload.getStatus().equals(StatusType.UNLINK)) {
            for (BookEntity book : books) {
                book2UserRepository.deleteByBookAndUser(book, userEntity);
            }
            return resultDto;
        }
        Book2UserTypeEntity code = book2UserTypeRepository.findByCode(payload.getStatus());
        for (BookEntity book : books) {
            String status = book2UserRepository.getCodeByBookSlugAndEmail(book.getSlug(), facade.getCurrentUsername());
            if (book2UserRepository.existsByUserAndBook(userEntity, book) && !payload.getStatus().equals(StatusType.RECENTLY_VIEWED)) {
                book2UserRepository.updateTypeByUserAndBook(code, userEntity, book);
            } else if (book2UserRepository.existsByUserAndBook(userEntity, book) && status.equals(StatusType.RECENTLY_VIEWED.toString())) {
                book2UserRepository.updateTypeByUserAndBook(code, userEntity, book);
            } else if (!book2UserRepository.existsByUserAndBook(userEntity, book)){
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

    @Scheduled(fixedRate = 60_000, zone = "Europe/Moscow")
    @Transactional
    public void deleteRecentlyViewedBooks() {
        book2UserRepository.deleteByType_CodeAndTimeBefore(StatusType.RECENTLY_VIEWED, LocalDateTime.now().minusMinutes(1));
    }

    @Override
    public BooksPageDto getBooksByStatus(StatusType status) {
        List<ShortBookDtoProjection> books = bookRepository.getBooksByUserAndStatus(
                facade.getCurrentUsername(), status.toString());
        return BooksPageDto.builder()
                .books(books)
                .totalPrice(books.stream()
                        .map(ShortBookDtoProjection::getPrice)
                        .mapToInt(Integer::intValue)
                        .sum())
                .totalDiscountPrice(books.stream()
                        .map(ShortBookDtoProjection::getDiscountPrice)
                        .mapToInt(Integer::intValue)
                        .sum())
                .slugs(books.stream()
                        .map(ShortBookDtoProjection::getSlug)
                        .collect(Collectors.toList()))
                .build();

    }

    @Override
    public String getBookStatus(String slug) {
        return book2UserRepository.getCodeByBookSlugAndEmail(slug, facade.getCurrentUsername());
    }

    @Override
    public Map<String, List<String>> getUserBookSlugs() {
        Map<String, List<String>> slugsByStatus = new HashMap<>();
        if (facade.getAuthentication() == null || facade.getAuthentication() instanceof AnonymousAuthenticationToken) {
            return getSlugsFromCookies();
        }
        for (StatusType statusType :
                StatusType.values()) {
            List<String> slugs = bookRepository
                    .findBookSlugsByUserEmail(facade.getCurrentUsername()).stream()
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
