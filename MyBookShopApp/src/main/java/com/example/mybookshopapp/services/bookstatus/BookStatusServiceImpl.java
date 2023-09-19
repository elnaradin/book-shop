package com.example.mybookshopapp.services.bookstatus;

import com.example.mybookshopapp.config.security.IAuthenticationFacade;
import com.example.mybookshopapp.dto.ResultDto;
import com.example.mybookshopapp.dto.admin.AdminChangeBookStatusDto;
import com.example.mybookshopapp.dto.book.BookSlugs;
import com.example.mybookshopapp.dto.book.BooksPageDto;
import com.example.mybookshopapp.dto.book.ShortBookDtoProjection;
import com.example.mybookshopapp.dto.book.status.ChangeBookStatusDto;
import com.example.mybookshopapp.model.book.BookEntity;
import com.example.mybookshopapp.model.book.links.Book2UserEntity;
import com.example.mybookshopapp.model.book.links.Book2UserTypeEntity;
import com.example.mybookshopapp.model.enums.StatusType;
import com.example.mybookshopapp.model.user.UserEntity;
import com.example.mybookshopapp.repositories.Book2UserRepository;
import com.example.mybookshopapp.repositories.Book2UserTypeRepository;
import com.example.mybookshopapp.repositories.BookRepository;
import com.example.mybookshopapp.repositories.UserRepository;
import com.example.mybookshopapp.services.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.mybookshopapp.errs.Messages.getMessageForLocale;


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
    public ResultDto changeBookStatus(ChangeBookStatusDto payload) {
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
            boolean existsByUserAndBook = book2UserRepository.existsByUserAndBook(userEntity, book);
            if (existsByUserAndBook && !payload.getStatus().equals(StatusType.RECENTLY_VIEWED)) {
                book2UserRepository.updateTypeByUserAndBook(code, userEntity, book);
            } else if (existsByUserAndBook && status.equals(StatusType.RECENTLY_VIEWED.toString())) {
                book2UserRepository.updateTypeByUserAndBook(code, userEntity, book);
            } else if (!existsByUserAndBook) {
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
        book2UserRepository.deleteByTypeCodeAndTimeBefore(StatusType.RECENTLY_VIEWED, LocalDateTime.now().minusMinutes(1));
    }

    @Override
    public BooksPageDto getBooksByStatus(StatusType status) {
        List<ShortBookDtoProjection> books = bookRepository.getBooksByUserAndStatus(
                facade.getCurrentUsername(), status.toString() );
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
    public String getBookStatus(String slug) {
        changeBookStatus(new ChangeBookStatusDto(List.of(slug), StatusType.RECENTLY_VIEWED));
        return book2UserRepository.getCodeByBookSlugAndEmail(slug, facade.getCurrentUsername());
    }

    @Override
    public Map<String, List<String>> getUserBookSlugs() {
        Map<String, List<String>> slugsByStatus = new HashMap<>();
        if (!facade.isAuthenticated()) {
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

    @Override
    public BooksPageDto getBoughtBooks(String userId) {
        List<ShortBookDtoProjection> books = new ArrayList<>();
        books.addAll(bookRepository.getBooksByUserAndStatus(userId, StatusType.PAID.toString()));
        books.addAll(bookRepository.getBooksByUserAndStatus(userId, StatusType.ARCHIVED.toString()));
        return BooksPageDto.builder()
                .books(books)
                .build();
    }

    @Override
    @Transactional
    public ResultDto changeBookStatusAdmin(AdminChangeBookStatusDto changeBookStatusDto) {
        ResultDto resultDto = new ResultDto();
        UserEntity user = userRepository.findByEmail(changeBookStatusDto.getUserId()).orElseThrow();
        BookEntity book = bookRepository.findBySlug(changeBookStatusDto.getBookId()).orElseThrow();
        if (changeBookStatusDto.getStatus() == StatusType.UNLINK) {
            long count = book2UserRepository.deleteByBookAndUser(book, user);
            if (count > 0) {
                resultDto.setResult(true);
                return resultDto;
            }
            resultDto.setError(getMessageForLocale("error.bookLinkNotDeleted"));
        }
        Book2UserTypeEntity type = book2UserTypeRepository.findByCode(StatusType.PAID);
        Book2UserEntity book2UserEntity = book2UserRepository.findByBookAndUser(book, user)
                .orElse(Book2UserEntity.builder()
                        .book(book)
                        .user(user)
                        .build());
        book2UserEntity.setTime(LocalDateTime.now());
        StatusType status = Optional.ofNullable(book2UserEntity.getType())
                .map(Book2UserTypeEntity::getCode)
                .orElse(null);
        if (status == StatusType.PAID || status == StatusType.ARCHIVED) {
            resultDto.setError(getMessageForLocale("error.bookAlreadyBought"));
            return resultDto;
        }

        book2UserEntity.setType(type);
        book2UserRepository.save(book2UserEntity);
        resultDto.setResult(true);
        return resultDto;
    }

    private Map<String, List<String>> getSlugsFromCookies() {
        Map<String, List<String>> slugsMap = new HashMap<>();
        slugsMap.put(StatusType.CART.toString(), cookieUtils.getSlugsFromCookie(StatusType.CART));
        slugsMap.put(StatusType.KEPT.toString(), cookieUtils.getSlugsFromCookie(StatusType.KEPT));
        return slugsMap;
    }

}
