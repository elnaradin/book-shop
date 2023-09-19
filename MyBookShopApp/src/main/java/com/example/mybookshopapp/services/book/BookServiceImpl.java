package com.example.mybookshopapp.services.book;

import com.example.mybookshopapp.config.security.IAuthenticationFacade;
import com.example.mybookshopapp.dto.ResultDto;
import com.example.mybookshopapp.dto.admin.UploadBookDto;
import com.example.mybookshopapp.dto.book.BookDto;
import com.example.mybookshopapp.dto.book.BooksPageDto;
import com.example.mybookshopapp.dto.book.FullBookDto;
import com.example.mybookshopapp.dto.book.ShortBookDtoProjection;
import com.example.mybookshopapp.dto.book.request.RequestDto;
import com.example.mybookshopapp.errs.ItemNotFoundException;
import com.example.mybookshopapp.model.book.BookEntity;
import com.example.mybookshopapp.model.book.authors.AuthorEntity;
import com.example.mybookshopapp.model.book.file.BookFileEntity;
import com.example.mybookshopapp.model.book.file.BookFileTypeEntity;
import com.example.mybookshopapp.model.book.links.Book2AuthorEntity;
import com.example.mybookshopapp.model.enums.BookFileType;
import com.example.mybookshopapp.model.enums.FileType;
import com.example.mybookshopapp.model.enums.StatusType;
import com.example.mybookshopapp.repositories.AuthorRepository;
import com.example.mybookshopapp.repositories.Book2AuthorRepository;
import com.example.mybookshopapp.repositories.BookFileRepository;
import com.example.mybookshopapp.repositories.BookFileTypeRepository;
import com.example.mybookshopapp.repositories.BookRepository;
import com.example.mybookshopapp.repositories.GenreRepository;
import com.example.mybookshopapp.repositories.TagRepository;
import com.example.mybookshopapp.services.data.ResourceStorage;
import com.example.mybookshopapp.services.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.mybookshopapp.errs.Messages.getMessageForLocale;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final CookieUtils cookieUtils;
    private final IAuthenticationFacade facade;
    private final ResourceStorage storage;
    private final GenreRepository genreRepository;
    private final TagRepository tagRepository;
    private final AuthorRepository authorRepository;
    private final Book2AuthorRepository book2AuthorRepository;
    private final BookFileTypeRepository bookFileTypeEntityRepository;
    private final BookFileRepository bookFileRepository;

    @Override
    @Transactional
    public ResultDto saveOrUpdateBook(UploadBookDto uploadBookDto) throws IOException {
        BookEntity book;
        if (uploadBookDto.getOldSlug() == null) {
            book = new BookEntity();
        } else {
            book = bookRepository.findBySlug(uploadBookDto.getOldSlug()).orElse(new BookEntity());
        }
        book.setTitle(uploadBookDto.getTitle());
        book.setSlug(uploadBookDto.getSlug());
        book.setPrice(uploadBookDto.getPrice());
        book.setDiscount(uploadBookDto.getDiscount());
        book.setIsBestseller(uploadBookDto.getIsBestseller());
        book.setDescription(uploadBookDto.getDescription());
        book.setPubDate(uploadBookDto.getPubDate());
        book.setGenres(genreRepository.findBySlugIn(uploadBookDto.getGenres()));
        book.setTags(tagRepository.findBySlugIn(uploadBookDto.getTags()));
        bookRepository.save(book);
        if (uploadBookDto.getImage() != null) {
            book.setImage(storage.saveNewFile(uploadBookDto.getSlug(), uploadBookDto.getImage(), FileType.BOOK_COVER));
        }
        saveLinksToAuthors(uploadBookDto, book);
        saveBookFile(book, BookFileType.PDF, uploadBookDto.getPdf());
        saveBookFile(book, BookFileType.EPUB, uploadBookDto.getEpub());
        saveBookFile(book, BookFileType.FB2, uploadBookDto.getFb2());
        return ResultDto.builder()
                .result(true)
                .build();
    }

    @Override
    public BookDto getBookUpdateData(String slug) {
        return bookRepository.getBookDtoBySlug(slug);
    }

    @Override
    @Transactional
    public ResultDto deleteBook(String slug) {
        ResultDto resultDto = new ResultDto();
        long count = bookRepository.deleteBySlug(slug);
        log.info("books deleted: " + count);
        if (count > 0) {
            resultDto.setResult(true);
            return resultDto;
        }
        resultDto.setError(getMessageForLocale("exceptionMessage.itemNotFound.book"));
        return resultDto;
    }

    private void saveLinksToAuthors(UploadBookDto uploadBookDto, BookEntity book) {
        List<AuthorEntity> authorEntities = authorRepository.findBySlugIn(uploadBookDto.getAuthors());
        int sortIndex = 0;
        List<Book2AuthorEntity> book2AuthorEntities = new ArrayList<>();
        long count = book2AuthorRepository.deleteByBook(book);
        log.info("links deleted:" + count);
        for (String slug : uploadBookDto.getAuthors()) {
            AuthorEntity author = authorEntities.stream()
                    .filter(a -> a.getSlug().equals(slug))
                    .findFirst()
                    .orElse(null);
            Book2AuthorEntity link = Book2AuthorEntity.builder()
                    .book(book)
                    .author(author)
                    .sortIndex(sortIndex++)
                    .build();
            if (!book2AuthorRepository.existsByBookAndAuthor(book, author)) {
                book2AuthorEntities.add(link);
            }
        }
        book2AuthorRepository.saveAll(book2AuthorEntities);
    }

    private void saveBookFile(BookEntity book, BookFileType fileType, MultipartFile multipartFile) throws IOException {
        if (multipartFile == null) {
            return;
        }
        BookFileTypeEntity type = bookFileTypeEntityRepository.findByName(fileType.toString());
        bookFileRepository.deleteByBookAndType(book, type);
        BookFileEntity fileEntity = BookFileEntity.builder()
                .book(book)
                .hash(UUID.randomUUID().toString())
                .type(type)
                .path(storage.saveNewFile(book.getSlug(), multipartFile, FileType.BOOK))
                .build();
        if (!bookFileRepository.existsByBookAndType(book, type)) {
            bookFileRepository.save(fileEntity);
        }


    }

    @Override
    public FullBookDto getFullBookInfoBySlug(String bookSlug) {
        return bookRepository.getFullBookInfo(bookSlug)
                .orElseThrow(() -> new ItemNotFoundException("exceptionMessage.itemNotFound.book"));
    }

    @Override
    public BooksPageDto getRecommendedBooksPage(RequestDto requestDto) {
        Pageable nextPage = PageRequest.of(requestDto.getOffset(), requestDto.getLimit());
        List<String> slugsToExclude = getSlugsToExclude();
        Page<ShortBookDtoProjection> booksPage;
        if (CollectionUtils.isEmpty(slugsToExclude)) {
            booksPage = bookRepository
                    .getRecommendedBooks( nextPage);
        } else {
            booksPage = bookRepository
                    .getRecommendedBooks(slugsToExclude, nextPage);
        }

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
        List<String> slugsToExclude=getSlugsToExclude();
        Page<ShortBookDtoProjection> booksPage;
        LocalDate periodFrom = from == null ? LocalDate.ofYearDay(-4712, 366) : from;
        LocalDate periodTo = to == null ? LocalDate.now() : to;
        if(CollectionUtils.isEmpty(slugsToExclude)){
            booksPage = bookRepository.getRecentBooksByPubDate(
                    periodFrom,
                    periodTo,

                    nextPage
            );
        }else{
            booksPage = bookRepository.getRecentBooksByPubDate(
                    periodFrom,
                    periodTo,
                    slugsToExclude,
                    nextPage
            );
        }

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
        List<String> slugsToExclude = getSlugsToExclude();
        Page<ShortBookDtoProjection> booksPage;
        if(CollectionUtils.isEmpty(slugsToExclude)){
            booksPage = bookRepository.getPopularBooks(pageable);
        }else{
            booksPage = bookRepository.getPopularBooks(slugsToExclude, pageable);
        }

        return BooksPageDto.builder()
                .count(booksPage.getTotalElements())
                .books(booksPage.getContent())
                .hasNext(booksPage.hasNext())
                .build();
    }


    protected List<String> getSlugsToExclude() {
        if (facade.isAuthenticated()) {
            return bookRepository.findSlugsByUserEmail(facade.getCurrentUsername());
        }
        List<String> slugs = new ArrayList<>(cookieUtils.getBookSlugsByStatus(StatusType.KEPT));
        List<String> cart = cookieUtils.getBookSlugsByStatus(StatusType.CART);
        slugs.addAll(cart);
        return slugs;

    }

}
