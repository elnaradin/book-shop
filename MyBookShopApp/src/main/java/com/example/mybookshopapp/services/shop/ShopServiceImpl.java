package com.example.mybookshopapp.services.shop;

import com.example.mybookshopapp.config.security.IAuthenticationFacade;
import com.example.mybookshopapp.dto.ResultDto;
import com.example.mybookshopapp.dto.shop.DocumentDto;
import com.example.mybookshopapp.dto.shop.FaqDto;
import com.example.mybookshopapp.dto.shop.MessageDto;
import com.example.mybookshopapp.mapper.MessageMapper;
import com.example.mybookshopapp.model.book.review.MessageEntity;
import com.example.mybookshopapp.model.user.UserEntity;
import com.example.mybookshopapp.repositories.DocumentRepository;
import com.example.mybookshopapp.repositories.FaqRepository;
import com.example.mybookshopapp.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {
    private final DocumentRepository documentRepository;
    private final FaqRepository faqRepository;
    private final IAuthenticationFacade facade;
    private final MessageMapper mapper;
    private final MessageRepository messageRepository;

    @Override
    public List<DocumentDto> getAllDocuments() {
        return documentRepository.getAllDocuments();
    }

    @Override
    public DocumentDto getDocumentBySlug(String slug) {
        return documentRepository.getDocumentBySlug(slug);
    }

    @Override
    public List<FaqDto> getAllFaqs() {
        return faqRepository.getAllFaqs();
    }

    @Override
    public ResultDto saveMessage(MessageDto message) {
        ResultDto resultDto = new ResultDto();
        resultDto.setResult(true);
        MessageEntity messageEntity;
        if (facade.isAuthenticated()) {
            UserEntity user = facade.getPrincipal();
            messageEntity = mapper.toMessageEntity(message, user);
        } else {
            if (StringUtils.isBlank(message.getName()) || StringUtils.isBlank(message.getMail())) {
                resultDto.setResult(false);
                return resultDto;
            }
            messageEntity = mapper.toMessageEntity(message);
        }
        messageEntity.setTime(LocalDateTime.now());
        messageRepository.save(messageEntity);
        return resultDto;
    }
}
