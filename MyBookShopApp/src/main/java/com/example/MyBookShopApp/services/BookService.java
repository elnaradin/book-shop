package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.dto.BooksByGenreDto;
import com.example.MyBookShopApp.dto.BooksWithTagDto;
import com.example.MyBookShopApp.model.book.tags.TagEntity;
import com.example.MyBookShopApp.model.genre.GenreEntity;
import com.example.MyBookShopApp.repositories.Book2TagRepository;
import com.example.MyBookShopApp.repositories.BookRepository;
import com.example.MyBookShopApp.model.book.BookEntity;
import com.example.MyBookShopApp.repositories.GenreRepository;
import com.example.MyBookShopApp.repositories.TagRepository;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final TagRepository tagRepository;
    private final BooksRatingAndPopularityService booksRatingAndPopularityService;
    private final Book2TagRepository book2TagRepository;
    private final GenreRepository genreRepository;


    public BookService(BookRepository bookRepository,
                       TagRepository tagRepository,
                       BooksRatingAndPopularityService booksRatingAndPopularityService,
                       Book2TagRepository book2TagRepository,
                       GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.tagRepository = tagRepository;
        this.booksRatingAndPopularityService = booksRatingAndPopularityService;
        this.book2TagRepository = book2TagRepository;
        this.genreRepository = genreRepository;

    }


    public Page<BookEntity> getPageOfRecommendedBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findAll(nextPage);
    }

    public Page<BookEntity> getPageOfSearchResultBooks(String searchWord, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBookEntitiesByTitleContaining(searchWord, nextPage);
    }

    public Page<BookEntity> getPageOfRecentBooksByPubDate(String from, String to, Integer offset,
                                                          Integer limit) throws ParseException {
        Page<BookEntity> bookEntityPage;
        if (from == null || from.equals("0")) {
            bookEntityPage = getPageOfRecentBooksFromMonthAgo(offset, limit);
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            Date dateFrom = formatter.parse(from);
            Date dateTo = formatter.parse(to);
            Pageable nextPage = PageRequest.of(offset, limit);
            bookEntityPage = bookRepository
                    .findBookEntitiesByPubDateBetweenOrderByPubDateDesc(dateFrom, dateTo, nextPage);
        }

        return bookEntityPage;
    }

    public Page<BookEntity> getPageOfRecentBooksFromMonthAgo(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        Date dateFrom = Date.from(ZonedDateTime.now().minusMonths(1).toInstant());
        return bookRepository
                .findBookEntitiesByPubDateBetweenOrderByPubDateDesc(dateFrom, new Date(), nextPage);
    }


    public List<BookEntity> getListOfPopularBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        Page<BookEntity> bookEntityPage = bookRepository.findAll(nextPage);
        List<BookEntity> popularBooks = bookEntityPage.getContent().stream()
                .sorted((o1, o2) -> booksRatingAndPopularityService.calculateBookPopularity(o1)
                        .compareTo(booksRatingAndPopularityService.calculateBookPopularity(o2)))
                .collect(Collectors.toList());
        Collections.reverse(popularBooks);
        return popularBooks;
    }

    public BooksWithTagDto getTaggedBooks(Integer tagId, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        Optional<TagEntity> tag = tagRepository.findById(tagId);
        if(tag.isEmpty()){
            throw new NullPointerException();
        }

        return new BooksWithTagDto(tag.get(),
                bookRepository.findBookEntitiesByTagsIn(tag.get().getBooks(), nextPage).getContent());
    }

    public List<TagEntity> getTagsList(){
        return tagRepository.findAll();
    }

    public Integer getMaxTagValue(){
        return book2TagRepository.findMaxTagValue();
    }
    public Integer getMinTagValue(){
        return book2TagRepository.findMinTagValue();
    }

    public List<GenreEntity> getGenres(){
        return genreRepository.findAll();
    }

    public BooksByGenreDto getBooksByGenre(String slug, Integer offset, Integer limit) {
        Optional<GenreEntity> genre = genreRepository.findGenreEntitiesBySlugIs(slug);
        if(genre.isEmpty()){
            throw new NullPointerException();
        }
        Pageable nextPge = PageRequest.of(offset, limit);
        return new BooksByGenreDto(genre.get(),
                bookRepository.findBookEntitiesByGenresIn(genre.get().getBooks(),
                nextPge).getContent());
    }
}
