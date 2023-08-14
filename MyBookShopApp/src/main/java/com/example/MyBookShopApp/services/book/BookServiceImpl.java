package com.example.MyBookShopApp.services.book;

import com.example.MyBookShopApp.dto.book.BooksPageDto;
import com.example.MyBookShopApp.dto.book.FullBookDto;
import com.example.MyBookShopApp.dto.book.ShortBookDto;
import com.example.MyBookShopApp.dto.request.RequestDto;
import com.example.MyBookShopApp.errs.NotFoundException;
import com.example.MyBookShopApp.model.enums.StatusType;
import com.example.MyBookShopApp.repositories.BookRepository;
import com.example.MyBookShopApp.services.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final CookieUtils cookieUtils;

    @Override
    public FullBookDto getFullBookInfoBySlug(String bookSlug) {

        FullBookDto fullBookInfo = bookRepository.getFullBookInfo(bookSlug);
        if(fullBookInfo == null){
            throw new NotFoundException();
        }
        return fullBookInfo;
    }
    @Override
    public BooksPageDto getRecommendedBooksPage(RequestDto requestDto) {
        Pageable nextPage = PageRequest.of(requestDto.getOffset(), requestDto.getLimit());

        Page<ShortBookDto> booksPage = bookRepository
                .getRecommendedBooks(getSlugsToExclude(), nextPage);
        return BooksPageDto.builder()
                .count(booksPage.getTotalElements())
                .books(booksPage.getContent())
                .hasNext(booksPage.hasNext())
                .build();
    }

    @Override
    public BooksPageDto getRecentBooksPage(RequestDto requestDto) {
        Pageable nextPage = PageRequest.of(requestDto.getOffset(), requestDto.getLimit());
        LocalDate from = requestDto.getFrom();
        LocalDate to = requestDto.getTo();
        Page<ShortBookDto> booksPage = bookRepository.getRecentBooksByPubDate(
                from == null ? LocalDate.ofYearDay(-4712, 366) : from,
                to == null ? LocalDate.now() : to,
                getSlugsToExclude(),
                nextPage
        );
        return BooksPageDto.builder()
                .from(requestDto.getFrom())
                .to(requestDto.getTo())
                .count(booksPage.getTotalElements())
                .books(booksPage.getContent())
                .hasNext(booksPage.hasNext())
                .build();
    }
    @Override
    public BooksPageDto getPopularBooksPage(RequestDto request) {
        Pageable pageable = PageRequest.of(request.getOffset(), request.getLimit());
        Page<ShortBookDto> booksPage = bookRepository.getPopularBooks(getSlugsToExclude(), pageable);
        return BooksPageDto.builder()
                .count(booksPage.getTotalElements())
                .books(booksPage.getContent())
                .hasNext(booksPage.hasNext())
                .build();
    }
    @Override
    public BooksPageDto getPageOfSearchResultBooks(RequestDto request) {
        Pageable nextPage = PageRequest.of(request.getOffset(), request.getLimit());
        Page<ShortBookDto> booksPage = bookRepository
                .findBooksByTitleContaining(request.getSearchWord(), nextPage);
        return BooksPageDto.builder()
                .count(booksPage.getTotalElements())
                .books(booksPage.getContent())
                .hasNext(booksPage.hasNext())
                .build();
    }
    protected List<String> getSlugsToExclude(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> slugs = new ArrayList<>();
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken){
            List<String> kept = cookieUtils.getBookSlugsByStatus(StatusType.KEPT);
            slugs.addAll(kept == null ? new ArrayList<>() : kept);
            List<String> cart = cookieUtils.getBookSlugsByStatus(StatusType.CART);
            slugs.addAll(cart == null ? new ArrayList<>() : cart);
            return slugs;
        }
        return bookRepository.findSlugsByUserEmail(authentication.getName());
    }

}
