package com.example.mybookshopapp.mapper;

import com.example.mybookshopapp.dto.shop.MessageDto;
import com.example.mybookshopapp.model.book.review.MessageEntity;
import com.example.mybookshopapp.model.user.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    @Mapping(source = "mail", target = "email")
    @Mapping(source = "topic", target = "subject")
    @Mapping(source = "message", target = "text")
    MessageEntity toMessageEntity(MessageDto messageDto);

    @Mapping(source = "user", target = "user")
    @Mapping(source = "user.name", target = "name")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "message.topic", target = "subject")
    @Mapping(source = "message.message", target = "text")
    MessageEntity toMessageEntity(MessageDto message, UserEntity user);
}
