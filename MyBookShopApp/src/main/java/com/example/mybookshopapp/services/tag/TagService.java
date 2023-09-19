package com.example.mybookshopapp.services.tag;

import com.example.mybookshopapp.dto.ResultDto;
import com.example.mybookshopapp.dto.book.BooksPageDto;
import com.example.mybookshopapp.dto.book.request.RequestDto;
import com.example.mybookshopapp.dto.tag.ShortTagDto;
import com.example.mybookshopapp.dto.tag.TagDtoProjection;
import com.example.mybookshopapp.dto.admin.UploadTagDto;

import java.util.List;

public interface TagService {
    List<TagDtoProjection> getTagsList();

    List<ShortTagDto> getShortTagsList(String slug);

    BooksPageDto getBooksPageByTag(RequestDto request);

    ShortTagDto getShortTagInfo(String tagSlug);

    List<ShortTagDto> getSortedTagsList();

    ResultDto saveOrUpdateTag(UploadTagDto uploadTagDto);

    ResultDto deleteTag(String slug);
}
