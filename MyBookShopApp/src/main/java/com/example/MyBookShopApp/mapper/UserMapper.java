package com.example.MyBookShopApp.mapper;

import com.example.MyBookShopApp.dto.user.SmallUserDto;
import com.example.MyBookShopApp.model.user.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    SmallUserDto toSmallUserDto(UserEntity userEntity);
}
