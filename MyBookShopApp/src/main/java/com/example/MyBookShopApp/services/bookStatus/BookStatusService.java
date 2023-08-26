package com.example.MyBookShopApp.services.bookStatus;

import com.example.MyBookShopApp.dto.ResultDto;
import com.example.MyBookShopApp.dto.book.BooksPageDto;
import com.example.MyBookShopApp.dto.book.ChangeStatusPayload;
import com.example.MyBookShopApp.model.enums.StatusType;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface BookStatusService {
    BooksPageDto getAnonymUserBooks(StatusType status);



    @Transactional
    ResultDto changeBookStatus(ChangeStatusPayload payload);

    BooksPageDto getBooksByStatus(StatusType status);

    String getBookStatus(String slug);

    Map<String, List<String>> getUserBookSlugs();
}
