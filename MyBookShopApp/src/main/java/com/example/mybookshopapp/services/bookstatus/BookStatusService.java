package com.example.mybookshopapp.services.bookstatus;

import com.example.mybookshopapp.dto.ResultDto;
import com.example.mybookshopapp.dto.admin.AdminChangeBookStatusDto;
import com.example.mybookshopapp.dto.book.BooksPageDto;
import com.example.mybookshopapp.dto.book.status.ChangeBookStatusDto;
import com.example.mybookshopapp.model.enums.StatusType;

import java.util.List;
import java.util.Map;

public interface BookStatusService {
    BooksPageDto getAnonymUserBooks(StatusType status);

    ResultDto changeBookStatus(ChangeBookStatusDto payload);

    BooksPageDto getBooksByStatus(StatusType status);

    String getBookStatus(String slug);

    Map<String, List<String>> getUserBookSlugs();

    BooksPageDto getBoughtBooks(String userId);

    ResultDto changeBookStatusAdmin(AdminChangeBookStatusDto changeBookStatusDto);
}
