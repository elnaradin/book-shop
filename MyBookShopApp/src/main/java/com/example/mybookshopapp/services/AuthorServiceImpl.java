package com.example.mybookshopapp.services;

import com.example.mybookshopapp.annotation.DurationTrackable;
import com.example.mybookshopapp.dto.ResultDto;
import com.example.mybookshopapp.dto.admin.UploadAuthorDto;
import com.example.mybookshopapp.dto.author.AuthorDto;
import com.example.mybookshopapp.dto.author.FullAuthorDto;
import com.example.mybookshopapp.dto.author.ShortAuthorDto;
import com.example.mybookshopapp.dto.book.BooksPageDto;
import com.example.mybookshopapp.dto.book.ShortBookDtoProjection;
import com.example.mybookshopapp.dto.book.request.RequestDto;
import com.example.mybookshopapp.errs.ItemNotFoundException;
import com.example.mybookshopapp.model.book.authors.AuthorEntity;
import com.example.mybookshopapp.model.enums.FileType;
import com.example.mybookshopapp.repositories.AuthorRepository;
import com.example.mybookshopapp.repositories.BookRepository;
import com.example.mybookshopapp.services.data.ResourceStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.mybookshopapp.errs.Messages.getMessageForLocale;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorServiceImpl implements AuthorService {
    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;
    private final ResourceStorage storage;
    private static final String AUTHOR_ERROR_MESSAGE ="exceptionMessage.itemNotFound.author";

    @Override
    @Transactional
    public ResultDto saveOrUpdateAuthor(UploadAuthorDto uploadAuthorDto) throws IOException {
        AuthorEntity author;
        if (uploadAuthorDto.getOldSlug() == null) {
            author = new AuthorEntity();
        } else {
            author = authorRepository.findBySlug(uploadAuthorDto.getOldSlug()).orElse(new AuthorEntity());
        }
        if (uploadAuthorDto.getPhoto() != null) {
            String path = storage.saveNewFile(uploadAuthorDto.getSlug(), uploadAuthorDto.getPhoto(), FileType.AUTHOR_PHOTO);
            author.setPhoto(path);
        }
        author.setName(uploadAuthorDto.getName());
        author.setSlug(uploadAuthorDto.getSlug());
        author.setDescription(uploadAuthorDto.getDescription());
        authorRepository.save(author);
        return ResultDto.builder().result(true).build();
    }

    @Override
    public AuthorDto getAuthorUpdateData(String slug) {
        return authorRepository.getAuthorDtoBySlug(slug);
    }

    @Override
    @Transactional
    public ResultDto deleteAuthor(String slug) {
        ResultDto resultDto = new ResultDto();
        long count = authorRepository.deleteBySlug(slug);
        log.info("authors deleted: " + count);
        if (count > 0) {
            resultDto.setResult(true);
            return resultDto;
        }
        resultDto.setError(getMessageForLocale(AUTHOR_ERROR_MESSAGE));
        return resultDto;
    }

    @DurationTrackable
    @Override
    public Map<String, List<ShortAuthorDto>> createAuthorsMap() {
        List<ShortAuthorDto> authors = authorRepository.findAllAuthors();
        return authors.stream().sorted(Comparator.comparing(ShortAuthorDto::getName)).collect(Collectors
                .groupingBy((ShortAuthorDto a) -> a.getName().substring(0, 1)));
    }

    @Override
    public FullAuthorDto getFullAuthorInfo(String slug) {
        Optional<FullAuthorDto> author = authorRepository.getFullAuthorDtoBySlug(slug);
        return author.orElseThrow(() -> new ItemNotFoundException(AUTHOR_ERROR_MESSAGE));
    }

    @Override
    public BooksPageDto getBooksPageByAuthor(RequestDto request) {
        Pageable nextPage = PageRequest.of(request.getOffset(), request.getLimit());
        Page<ShortBookDtoProjection> booksPage = bookRepository.getBooksListByAuthor(
                request.getSlug(),  nextPage
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
        return shortAuthorDto.orElseThrow(() -> new ItemNotFoundException(AUTHOR_ERROR_MESSAGE));
    }

    @Override
    public List<ShortAuthorDto> getAuthorsListBySlug(String slug) {
        return authorRepository.getAuthorsByBookSlug(slug);
    }

    @Override
    public List<ShortAuthorDto> getSortedAuthorList() {
        return authorRepository.findAllAuthors();
    }


}
