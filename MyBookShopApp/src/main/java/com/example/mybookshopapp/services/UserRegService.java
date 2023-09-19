package com.example.mybookshopapp.services;

import com.example.mybookshopapp.dto.ResultDto;
import com.example.mybookshopapp.dto.book.request.RequestDto;
import com.example.mybookshopapp.dto.payment.ChangeBalanceDto;
import com.example.mybookshopapp.dto.payment.yookassa.request.ProfileUpdateDto;
import com.example.mybookshopapp.dto.security.ContactConfirmationPayload;
import com.example.mybookshopapp.dto.security.ContactConfirmationResponse;
import com.example.mybookshopapp.dto.security.RegistrationForm;
import com.example.mybookshopapp.dto.user.SmallUserDto;
import com.example.mybookshopapp.dto.user.UserDtoPage;
import com.example.mybookshopapp.model.user.UserEntity;

import javax.transaction.Transactional;

public interface UserRegService {
    UserEntity registerNewUser(RegistrationForm registrationForm);

    ContactConfirmationResponse jwtLogin(ContactConfirmationPayload payload);

    @Transactional
    ContactConfirmationResponse jwtLoginByPhone(ContactConfirmationPayload payload);

    ContactConfirmationResponse jwtLoginByEmail(ContactConfirmationPayload payload);

    ResultDto updateProfile(ProfileUpdateDto updateDto);


    void changeBalance(ChangeBalanceDto changeBalanceDto);

    SmallUserDto getShortUserInfo();

    UserDtoPage getUserPage(RequestDto request);

    ResultDto deleteUser(String itemId);
}
