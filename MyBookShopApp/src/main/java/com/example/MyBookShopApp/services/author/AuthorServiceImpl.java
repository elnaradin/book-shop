package com.example.MyBookShopApp.services.author;

import com.example.MyBookShopApp.dto.author.FullAuthorDto;
import com.example.MyBookShopApp.dto.author.ShortAuthorDto;
import com.example.MyBookShopApp.dto.book.BooksPageDto;
import com.example.MyBookShopApp.dto.book.ShortBookDto;
import com.example.MyBookShopApp.dto.request.RequestDto;
import com.example.MyBookShopApp.errs.NotFoundException;
import com.example.MyBookShopApp.repositories.AuthorRepository;
import com.example.MyBookShopApp.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService{
    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    @Override
    public Map<String, List<ShortAuthorDto>> createAuthorsMap() {
        List<ShortAuthorDto> authors = authorRepository.findAllAuthors();
        return authors.stream().sorted(Comparator.comparing(ShortAuthorDto::getName)).collect(Collectors
                .groupingBy((ShortAuthorDto a) -> a.getName().substring(0, 1)));
    }

    @Override
    public FullAuthorDto getFullAuthorInfo(String slug) {
        Optional<FullAuthorDto> author = authorRepository.getFullAuthorDtoBySlug(slug);
        return author.orElseThrow(NotFoundException::new);
    }

    @Override
    public BooksPageDto getBooksPageByAuthor(RequestDto request) {
        Pageable nextPage = PageRequest.of(request.getOffset(), request.getLimit());
        Page<ShortBookDto> booksPage = bookRepository.getBooksListByAuthor(
                request.getSlug(), nextPage
        );
        Long booksCount = booksPage.getTotalElements();
        return BooksPageDto.builder()
                .count(booksCount)
                .books(booksPage.getContent())
                .hasNext(booksPage.hasNext())
                .build();
    }

    @Override
    public ShortAuthorDto getShortAuthorInfo(String slug) {
        Optional<ShortAuthorDto> shortAuthorDto = authorRepository.getShortAuthorDtoBySlug(slug);
        return shortAuthorDto.orElseThrow(NotFoundException::new);
    }

    @Override
    public List<ShortAuthorDto> getAuthorsList(String slug) {
        return authorRepository.getAuthorsByBookSlug(slug);
    }
}
