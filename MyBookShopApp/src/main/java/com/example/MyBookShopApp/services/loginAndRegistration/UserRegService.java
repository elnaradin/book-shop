package com.example.MyBookShopApp.services.loginAndRegistration;

import com.example.MyBookShopApp.dto.ResultDto;
import com.example.MyBookShopApp.dto.payment.ChangeBalanceDto;
import com.example.MyBookShopApp.dto.payment.yookassa.request.ProfileUpdateDto;
import com.example.MyBookShopApp.dto.security.ContactConfirmationPayload;
import com.example.MyBookShopApp.dto.security.ContactConfirmationResponse;
import com.example.MyBookShopApp.dto.security.RegistrationForm;
import com.example.MyBookShopApp.model.user.UserEntity;

import javax.transaction.Transactional;

public interface UserRegService {
    UserEntity registerNewUser(RegistrationForm registrationForm);

    ContactConfirmationResponse jwtLogin(ContactConfirmationPayload payload);

    @Transactional
    ContactConfirmationResponse jwtLoginByPhone(ContactConfirmationPayload payload);

    ContactConfirmationResponse jwtLoginByEmail(ContactConfirmationPayload payload);

    ResultDto updateProfile(ProfileUpdateDto updateDto, String name);


    void changeBalance(ChangeBalanceDto changeBalanceDto);
}
