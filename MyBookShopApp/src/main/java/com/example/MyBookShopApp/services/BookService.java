package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.dto.BookDto;
import com.example.MyBookShopApp.dto.BooksByGenreDto;
import com.example.MyBookShopApp.dto.BooksWithTagDto;
import com.example.MyBookShopApp.dto.GenreDto;
import com.example.MyBookShopApp.dto.TagDto;
import com.example.MyBookShopApp.model.book.tags.TagEntity;
import com.example.MyBookShopApp.model.enums.StatusType;
import com.example.MyBookShopApp.model.genre.GenreEntity;
import com.example.MyBookShopApp.repositories.AuthorRepository;
import com.example.MyBookShopApp.repositories.Book2TagRepository;
import com.example.MyBookShopApp.repositories.BookRepository;
import com.example.MyBookShopApp.model.book.BookEntity;
import com.example.MyBookShopApp.repositories.GenreRepository;
import com.example.MyBookShopApp.repositories.RatingRepository;
import com.example.MyBookShopApp.repositories.TagRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final TagRepository tagRepository;

    private final Book2TagRepository book2TagRepository;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;
    private final RatingRepository ratingRepository;


    public List<BookDto> getPageOfRecommendedBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        List<BookEntity> bookEntities = bookRepository.findAll(nextPage).getContent();
        return createBookList(bookEntities);
    }


    public List<BookDto> getPageOfSearchResultBooks(String searchWord, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        List<BookEntity> bookEntities = bookRepository
                .findBookEntitiesByTitleContaining(searchWord, nextPage).getContent();
        return createBookList(bookEntities);
    }

    public List<BookDto> getPageOfRecentBooksByPubDate(String from, String to, Integer offset,
                                                       Integer limit) throws ParseException {
        List<BookEntity> bookEntityPage;
        if (from == null || from.equals("0")) {
            return getPageOfRecentBooksFromMonthAgo(offset, limit);
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            formatter = formatter.withLocale(Locale.ROOT);
            LocalDate dateFrom = LocalDate.parse(from, formatter);
            LocalDate dateTo = LocalDate.parse(to, formatter);
            Pageable nextPage = PageRequest.of(offset, limit);
            bookEntityPage = bookRepository
                    .findBookEntitiesByPubDateBetweenOrderByPubDateDesc(dateFrom, dateTo, nextPage).getContent();
        }
        return createBookList(bookEntityPage);
    }

    public List<BookDto> getPageOfRecentBooksFromMonthAgo(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        LocalDate dateFrom = LocalDate.now().minusMonths(1);
        List<BookEntity> bookEntities = bookRepository
                .findBookEntitiesByPubDateBetweenOrderByPubDateDesc(dateFrom, LocalDate.now(), nextPage).getContent();

        return createBookList(bookEntities);
    }


    public BooksWithTagDto getTaggedBooks(Integer tagId, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        Optional<TagEntity> tag = tagRepository.findById(tagId);
        if (tag.isEmpty()) {
            throw new NullPointerException();
        }
        List<BookEntity> bookEntities = bookRepository
                .findBookEntitiesByTagIs(tag.get().getId(), nextPage).getContent();
        return new BooksWithTagDto(tag.get(), createBookList(bookEntities));
    }

    public List<TagDto> getTagsList() {
        List<TagEntity> tagEntities = tagRepository.findAll();
        List<TagDto> tags = new ArrayList<>();
        for (TagEntity tagEntity : tagEntities) {
            Integer booksAmount = bookRepository.countBookEntitiesByTag(tagEntity.getId());
            tags.add(new TagDto(tagEntity, booksAmount));
        }
        return tags;
    }

    public Integer getMaxTagValue() {
        return book2TagRepository.findMaxTagValue();
    }

    public Integer getMinTagValue() {
        return book2TagRepository.findMinTagValue();
    }

    public List<GenreDto> getGenres() {
        List<GenreEntity> genreEntities = genreRepository.findAll();
        List<GenreDto> genres = new ArrayList<>();
        for (GenreEntity genreEntity : genreEntities) {

            genres.add(new GenreDto(genreEntity, bookRepository
                    .countBookEntitiesByGenre(genreEntity.getId())));
        }
        return genres;
    }

    public BooksByGenreDto getBooksByGenre(String slug, Integer offset, Integer limit) throws NullPointerException {
        Optional<GenreEntity> genre = genreRepository.findGenreEntitiesBySlugIs(slug);
        if (genre.isEmpty()) {
            throw new NullPointerException();
        }
        Pageable nextPage = PageRequest.of(offset, limit);
        List<BookEntity> bookEntities = bookRepository
                .findBookEntitiesByGenre(genre.get().getId(),
                        nextPage).getContent();
        return new BooksByGenreDto(genre.get(), createBookList(bookEntities));
    }

    public List<BookDto> createBookList(List<BookEntity> bookEntities) {
        List<BookDto> books = new ArrayList<>();
        for (BookEntity bookEntity : bookEntities) {
            Integer rating = ratingRepository.getBookRating(bookEntity.getId());
            books.add(new BookDto(bookEntity,
                    authorRepository.findAuthorsByBookEntity(bookEntity.getId()),
                    StatusType.UNLINK.toString(), rating));
        }
        return books;
    }

    public BookDto getBookBySlug(String slug) {
        BookEntity bookEntity = bookRepository.findBookEntityBySlug(slug);
        Integer rating = ratingRepository.getBookRating(bookEntity.getId());
        return new BookDto(bookEntity,
                authorRepository.findAuthorsByBookEntity(bookEntity.getId()),
                StatusType.UNLINK.toString(), rating);

    }


    public Integer countFoundBooks(String example) {
        return bookRepository.countBookEntitiesByTitleContaining(example);
    }

    public List<TagEntity> getTagsByBookSlug(String slug) {
        return tagRepository.findTagEntitiesByBookSlug(slug);
    }
}
