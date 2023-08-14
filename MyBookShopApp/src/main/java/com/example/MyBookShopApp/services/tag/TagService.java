package com.example.MyBookShopApp.services.tag;

import com.example.MyBookShopApp.dto.book.BooksPageDto;
import com.example.MyBookShopApp.dto.request.RequestDto;
import com.example.MyBookShopApp.dto.tag.ShortTagDto;
import com.example.MyBookShopApp.dto.tag.TagDtoProjection;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface TagService {
    List<TagDtoProjection> getTagsList();

    List<ShortTagDto> getShortTagsList(String slug);

    BooksPageDto getBooksPageByTag(RequestDto request, Authentication authentication);

    ShortTagDto getShortTagInfo(String tagSlug);
}
