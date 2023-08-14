package com.example.MyBookShopApp.services.tag;

import com.example.MyBookShopApp.dto.book.BooksPageDto;
import com.example.MyBookShopApp.dto.book.ShortBookDto;
import com.example.MyBookShopApp.dto.request.RequestDto;
import com.example.MyBookShopApp.dto.tag.ShortTagDto;
import com.example.MyBookShopApp.dto.tag.TagDtoProjection;
import com.example.MyBookShopApp.errs.NotFoundException;
import com.example.MyBookShopApp.repositories.BookRepository;
import com.example.MyBookShopApp.repositories.TagRepository;
import com.example.MyBookShopApp.services.bookStatus.BookStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final BookRepository bookRepository;
    private final BookStatusService statusService;


    @Override
    public List<ShortTagDto> getShortTagsList(String slug) {
        return tagRepository.getTagsByBookSlug(slug);
    }

    @Override
    public List<TagDtoProjection> getTagsList() {
        return tagRepository.getAllTags();
    }

    @Override
    public BooksPageDto getBooksPageByTag(RequestDto request, Authentication authentication) {
        Pageable nextPage = PageRequest.of(
                request.getOffset(),
                request.getLimit()
        );
        Page<ShortBookDto> booksPage = bookRepository.getBooksByTag(
                request.getSlug(),
                nextPage
        );
        Map<String, List<String>> slugsByStatus = statusService.getUserBookSlugs(authentication);
        return BooksPageDto.builder()
                .count(booksPage.getTotalElements())
                .books(booksPage.getContent())
                .hasNext(booksPage.hasNext())
                .slugsByStatus(slugsByStatus)
                .build();
    }

    @Override
    public ShortTagDto getShortTagInfo(String tagSlug) {
        Optional<ShortTagDto> shortTagDtoBySlug = tagRepository.getShortTagDtoBySlug(tagSlug);
        return shortTagDtoBySlug.orElseThrow(NotFoundException::new);
    }
}
