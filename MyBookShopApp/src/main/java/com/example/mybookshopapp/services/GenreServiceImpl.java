package com.example.mybookshopapp.services;

import com.example.mybookshopapp.annotation.DurationTrackable;
import com.example.mybookshopapp.dto.ResultDto;
import com.example.mybookshopapp.dto.book.BooksPageDto;
import com.example.mybookshopapp.dto.book.ShortBookDtoProjection;
import com.example.mybookshopapp.dto.book.request.RequestDto;
import com.example.mybookshopapp.dto.genre.GenreDto;
import com.example.mybookshopapp.dto.genre.ShortGenreDto;
import com.example.mybookshopapp.dto.admin.UploadGenreDto;
import com.example.mybookshopapp.errs.ItemNotFoundException;
import com.example.mybookshopapp.model.genre.GenreEntity;
import com.example.mybookshopapp.repositories.BookRepository;
import com.example.mybookshopapp.repositories.GenreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.mybookshopapp.errs.Messages.getMessageForLocale;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final BookStatusService statusService;

    @Override
    public ResultDto saveOrUpdateGenre(UploadGenreDto uploadGenreDto) {
        GenreEntity genre;
        if(uploadGenreDto.getOldSlug() == null){
           genre = new GenreEntity();
        } else {
            genre = genreRepository.findBySlug(uploadGenreDto.getSlug()).orElse(new GenreEntity());
        }
        genre.setName(uploadGenreDto.getName());
        genre.setSlug(uploadGenreDto.getSlug());
        genre.setParent(genreRepository.findBySlug(uploadGenreDto.getParentId()).orElse(null));
        genreRepository.save(genre);
        return ResultDto.builder().result(true).build();
    }

    @Override
    public List<ShortGenreDto> getShortGenresListBySlug(String slug) {
        return genreRepository.getShortGenreDtosByBookSlug(slug);
    }

    @Override
    @Transactional
    public ResultDto deleteGenre(String slug) {
        ResultDto resultDto = new ResultDto();
        long count = genreRepository.deleteBySlug(slug);
        log.info("genres deleted: " + count);
        if (count > 0){
            resultDto.setResult(true);
            return resultDto;
        }
        resultDto.setError(getMessageForLocale("exceptionMessage.itemNotFound.genre"));
        return resultDto;
    }

    @DurationTrackable
    @Override
    public GenreDto getGenreTree() {
        List<GenreDto> allGenres = genreRepository.getAllGenres();
        GenreDto parentGenre = new GenreDto();
        List<GenreDto> firstChildren = allGenres.stream()
                .filter(genre -> genre.getParentId() == null)
                .collect(Collectors.toList());
        parentGenre.setChildren(firstChildren);
        allGenres.removeAll(firstChildren);
        return createGenreTreeRecursively(allGenres, parentGenre);
    }

    private GenreDto createGenreTreeRecursively(
            List<GenreDto> allGenres,
            GenreDto parentGenre
    ) {
        if (allGenres.isEmpty()) {
            return parentGenre;
        }
        for (GenreDto child : parentGenre.getChildren()) {
            Integer id = child.getId();
            List<GenreDto> children = allGenres.stream()
                    .filter(genre -> Objects.equals(genre.getParentId(), id))
                    .collect(Collectors.toList());
            child.setChildren(children);
            allGenres.removeAll(children);
            createGenreTreeRecursively(allGenres, child);
        }
        return parentGenre;
    }

    @Override
    public BooksPageDto getBooksPageByGenre(RequestDto request) {
        Pageable pageable = PageRequest.of(request.getOffset(), request.getLimit());
        Page<ShortBookDtoProjection> booksPage = bookRepository.getBooksByGenre(
                request.getSlug(),pageable);

        Map<String, List<String>> slugsByStatus = statusService.getUserBookSlugs();
        return BooksPageDto.builder()
                .count(booksPage.getTotalElements())
                .books(booksPage.getContent())
                .hasNext(booksPage.hasNext())
                .slugsByStatus(slugsByStatus)
                .build();
    }
    @Override
    public List<ShortGenreDto> getSortedGenreList() {
        return genreRepository.getShortGenreDtos();
    }
    @Override
    public ShortGenreDto getShortGenreInfo(String genreSlug) {
        Optional<ShortGenreDto> shortGenreDto = genreRepository.getShortGenreDto(genreSlug);
        return shortGenreDto.orElseThrow(()-> new ItemNotFoundException("exceptionMessage.itemNotFound.genre"));
    }
}
