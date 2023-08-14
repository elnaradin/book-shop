package com.example.MyBookShopApp.services.loginAndRegistration;

import com.example.MyBookShopApp.dto.security.ContactConfirmationPayload;
import com.example.MyBookShopApp.dto.security.ContactConfirmationResponse;
import com.example.MyBookShopApp.dto.security.RegistrationForm;
import com.example.MyBookShopApp.model.user.UserEntity;

public interface UserRegService {
    UserEntity registerNewUser(RegistrationForm registrationForm);

    ContactConfirmationResponse jwtLogin(ContactConfirmationPayload payload);
}
