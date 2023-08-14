package com.example.MyBookShopApp.services.genre;

import com.example.MyBookShopApp.annotation.DurationTrackable;
import com.example.MyBookShopApp.dto.book.BooksPageDto;
import com.example.MyBookShopApp.dto.book.ShortBookDto;
import com.example.MyBookShopApp.dto.genre.GenreDto;
import com.example.MyBookShopApp.dto.genre.ShortGenreDto;
import com.example.MyBookShopApp.dto.request.RequestDto;
import com.example.MyBookShopApp.errs.ItemNotFoundException;
import com.example.MyBookShopApp.repositories.BookRepository;
import com.example.MyBookShopApp.repositories.GenreRepository;
import com.example.MyBookShopApp.services.bookStatus.BookStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final BookStatusService statusService;

    @DurationTrackable
    @Override
    public GenreDto getGenreTree() {
        List<GenreDto> allGenres = genreRepository.getAllGenres();
        GenreDto parentGenre = new GenreDto();
        List<GenreDto> firstChildren = allGenres.stream()
                .filter((genre) -> genre.getParentId() == null)
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
                    .filter((genre) -> Objects.equals(genre.getParentId(), id))
                    .collect(Collectors.toList());
            child.setChildren(children);
            allGenres.removeAll(children);
            createGenreTreeRecursively(allGenres, child);
        }
        return parentGenre;
    }

    @Override
    public BooksPageDto getBooksPageByGenre(RequestDto request, Authentication authentication) {
        Pageable pageable = PageRequest.of(request.getOffset(), request.getLimit());
        Page<ShortBookDto> booksPage = bookRepository.getBooksByGenre(request.getSlug(), pageable);

        Map<String, List<String>> slugsByStatus = statusService.getUserBookSlugs(authentication);
        return BooksPageDto.builder()
                .count(booksPage.getTotalElements())
                .books(booksPage.getContent())
                .hasNext(booksPage.hasNext())
                .slugsByStatus(slugsByStatus)
                .build();

    }

    @Override
    public ShortGenreDto getShortGenreInfo(String genreSlug) {
        Optional<ShortGenreDto> shortGenreDto = genreRepository.getShortGenreDto(genreSlug);
        return shortGenreDto.orElseThrow(ItemNotFoundException::new);
    }
}
