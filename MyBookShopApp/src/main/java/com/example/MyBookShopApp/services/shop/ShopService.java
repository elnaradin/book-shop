package com.example.MyBookShopApp.services.shop;

import com.example.MyBookShopApp.dto.ResultDto;
import com.example.MyBookShopApp.dto.shop.DocumentDto;
import com.example.MyBookShopApp.dto.shop.FaqDto;
import com.example.MyBookShopApp.dto.shop.MessageDto;

import java.util.List;

public interface ShopService {
    List<DocumentDto> getAllDocuments();

    Object getDocumentBySlug(String slug);

    List<FaqDto> getAllFaqs();

    ResultDto saveMessage(MessageDto message);
}
