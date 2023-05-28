package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.dto.book.BookDto;
import com.example.MyBookShopApp.dto.book.BooksPageDto;
import com.example.MyBookShopApp.dto.book.SlugBookDto;
import com.example.MyBookShopApp.dto.genre.BooksByGenreDto;
import com.example.MyBookShopApp.dto.genre.GenreDto;
import com.example.MyBookShopApp.dto.mappers.BooksMapper;
import com.example.MyBookShopApp.dto.tag.BooksByTagDto;
import com.example.MyBookShopApp.dto.tag.TagDto;
import com.example.MyBookShopApp.errs.GenreNotFoundException;
import com.example.MyBookShopApp.errs.TagNotFoundException;
import com.example.MyBookShopApp.model.book.BookEntity;
import com.example.MyBookShopApp.model.book.tags.TagEntity;
import com.example.MyBookShopApp.model.enums.StatusType;
import com.example.MyBookShopApp.model.genre.GenreEntity;
import com.example.MyBookShopApp.model.user.UserEntity;
import com.example.MyBookShopApp.repositories.Book2TagRepository;
import com.example.MyBookShopApp.repositories.BookRepository;
import com.example.MyBookShopApp.repositories.GenreRepository;
import com.example.MyBookShopApp.repositories.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final TagRepository tagRepository;

    private final Book2TagRepository book2TagRepository;
    private final GenreRepository genreRepository;
    private final BooksMapper booksMapper;
    private final UserService userService;

    public BooksPageDto getPageOfRecommendedBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        List<BookEntity> bookEntities = bookRepository.findAll(nextPage).getContent();
        return new BooksPageDto(0, createBookList(bookEntities));
    }


    public BooksPageDto getPageOfSearchResultBooks(String searchWord, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        List<BookEntity> bookEntities = bookRepository
                .findBookEntitiesByTitleContaining(searchWord, nextPage).getContent();
        Integer count = bookRepository.countBookEntitiesByTitleContaining(searchWord);
        return new BooksPageDto(count, createBookList(bookEntities));
    }

    public BooksPageDto getPageOfRecentBooksByPubDate(String from, String to, Integer offset,
                                                      Integer limit) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        formatter = formatter.withLocale(Locale.ROOT);
        if (from == null) {
            return getPageOfRecentBooksFromMonthAgo(offset, limit);
        }
        LocalDate dateFrom = LocalDate.parse(from, formatter);
        LocalDate dateTo = LocalDate.parse(to, formatter);

        Integer booksCount = bookRepository.countByPubDateBetween(dateFrom, dateTo);
        List<BookEntity> bookEntityPage;
        Pageable nextPage = PageRequest.of(offset, limit);
        bookEntityPage = bookRepository
                .findBookEntitiesByPubDateBetweenOrderByPubDateDesc(
                        dateFrom,
                        dateTo,
                        nextPage
                ).getContent();

        return new BooksPageDto(booksCount, createBookList(bookEntityPage));
    }

    public BooksPageDto getPageOfRecentBooksFromMonthAgo(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        LocalDate dateFrom = LocalDate.now().minusMonths(1);
        List<BookEntity> bookEntities = bookRepository
                .findBookEntitiesByPubDateBetweenOrderByPubDateDesc(
                        dateFrom,
                        LocalDate.now(),
                        nextPage
                ).getContent();
        Integer booksCount = bookRepository.countByPubDateBetween(dateFrom, LocalDate.now());
        return new BooksPageDto(booksCount, createBookList(bookEntities));
    }


    public BooksByTagDto getTaggedBooks(Integer tagId, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        Optional<TagEntity> tag = tagRepository.findById(tagId);
        if (tag.isEmpty()) {
            throw new TagNotFoundException("No such taf in db: " + tagId);
        }
        List<BookEntity> bookEntities = bookRepository
                .findBookEntitiesByTagIs(tag.get().getId(), nextPage).getContent();
        Integer booksCount = bookRepository.countBookEntitiesByTag(tag.get().getId());
        return new BooksByTagDto(booksCount, tag.get(), createBookList(bookEntities));
    }

    public List<TagDto> getTagsList() {
        List<TagEntity> tagEntities = tagRepository.findAll();
        List<TagDto> tags = new ArrayList<>();
        for (TagEntity tagEntity : tagEntities) {
            Integer booksAmount = bookRepository.countBookEntitiesByTag(tagEntity.getId());
            tags.add(booksMapper.createTagDto(tagEntity, booksAmount));
        }
        return tags;
    }

    public Integer getMaxTagValue() {
        return book2TagRepository.findMaxTagValue();
    }

    public Integer getMinTagValue() {
        return book2TagRepository.findMinTagValue();
    }

    public GenreEntity getGenreTree() {
        List<GenreEntity> genreEntities = genreRepository.findParentGenreEntities();

        GenreEntity genreDto = new GenreEntity();
        genreDto.setChildren(genreEntities);
        return genreDto;
    }

    public Map<Integer, Long> getBookCountPerGenre() {
        List<GenreDto> list = bookRepository.countBooksByGenre();

        return list.stream()
                .collect(Collectors.toMap(GenreDto::getGenreId,
                        GenreDto::getBooksAmount));
    }

    public BooksByGenreDto getBooksByGenre(String genreSlug, Integer offset, Integer limit) {
        Optional<GenreEntity> genre = genreRepository.findGenreEntitiesBySlugIs(genreSlug);
        if (genre.isEmpty()) {
            throw new GenreNotFoundException("no such genre in db: " + genreSlug);
        }
        Pageable nextPage = PageRequest.of(offset, limit);
        List<BookEntity> bookEntities = bookRepository
                .findBookEntitiesByGenre(genre.get().getId(),
                        nextPage).getContent();
        Integer booksCount = bookRepository.countBookEntitiesByGenre(genreSlug);
        return new BooksByGenreDto(
                booksCount,
                genre.get().getName(),
                genre.get().getSlug(),
                createBookList(bookEntities)
        );
    }

    public List<BookDto> createBookList(List<BookEntity> bookEntities) {
        List<BookDto> books = new ArrayList<>();
        for (BookEntity bookEntity : bookEntities) {
            books.add(booksMapper.createBookDto(bookEntity));
        }

        return books;
    }

    public SlugBookDto getBookBySlug(String slug) {
        BookEntity bookEntity = bookRepository.findBookEntityBySlug(slug);
        return booksMapper.createSlugBookDto(bookEntity);

    }


    public Integer countFoundBooks(String example) {
        return bookRepository.countBookEntitiesByTitleContaining(example);
    }

    public BooksPageDto getPageOfMyBooks(StatusType status) {
        UserEntity curUser = userService.getCurUser();
        List<BookDto> books = createBookList(bookRepository.findBooksByUserIdAndStatus(
                curUser.getId(), status.toString())
        );
        return new BooksPageDto(books.size(), books);
    }


}
