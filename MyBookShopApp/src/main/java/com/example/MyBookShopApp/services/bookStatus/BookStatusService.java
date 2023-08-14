package com.example.MyBookShopApp.services.bookStatus;

import com.example.MyBookShopApp.dto.ResultDto;
import com.example.MyBookShopApp.dto.book.BooksPageDto;
import com.example.MyBookShopApp.dto.book.ChangeStatusPayload;
import com.example.MyBookShopApp.model.enums.StatusType;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;

public interface BookStatusService {
    BooksPageDto getAnonymUserBooks(StatusType status);
    ResultDto changeBookStatus(ChangeStatusPayload payload, String authentication);
    BooksPageDto getBooksByStatus(StatusType status, Authentication authentication);
    StatusType getBookStatus(String slug, String email);
    Map<String, List<String>> getUserBookSlugs(Authentication authentication);
}
