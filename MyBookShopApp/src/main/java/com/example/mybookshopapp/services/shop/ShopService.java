package com.example.mybookshopapp.services.shop;

import com.example.mybookshopapp.dto.ResultDto;
import com.example.mybookshopapp.dto.shop.DocumentDto;
import com.example.mybookshopapp.dto.shop.FaqDto;
import com.example.mybookshopapp.dto.shop.MessageDto;

import java.util.List;

public interface ShopService {
    List<DocumentDto> getAllDocuments();

    Object getDocumentBySlug(String slug);

    List<FaqDto> getAllFaqs();

    ResultDto saveMessage(MessageDto message);
}
