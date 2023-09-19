package com.example.mybookshopapp.mapper;

import com.example.mybookshopapp.dto.user.SmallUserDto;
import com.example.mybookshopapp.model.user.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    SmallUserDto toSmallUserDto(UserEntity userEntity);
}
