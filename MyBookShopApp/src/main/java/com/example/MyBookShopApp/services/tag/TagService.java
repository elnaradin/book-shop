package com.example.MyBookShopApp.services.tag;

import com.example.MyBookShopApp.dto.book.BooksPageDto;
import com.example.MyBookShopApp.dto.book.request.RequestDto;
import com.example.MyBookShopApp.dto.tag.ShortTagDto;
import com.example.MyBookShopApp.dto.tag.TagDtoProjection;

import java.util.List;

public interface TagService {
    List<TagDtoProjection> getTagsList();

    List<ShortTagDto> getShortTagsList(String slug);

    BooksPageDto getBooksPageByTag(RequestDto request);

    ShortTagDto getShortTagInfo(String tagSlug);
}
