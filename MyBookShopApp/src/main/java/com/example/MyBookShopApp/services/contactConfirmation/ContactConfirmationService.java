package com.example.MyBookShopApp.services.contactConfirmation;

import com.example.MyBookShopApp.dto.security.ContactConfirmationResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface ContactConfirmationService {
    ContactConfirmationResponse sendSmsLogin(String phone) throws JsonProcessingException;

    ContactConfirmationResponse sendEmailLogin(String to);

    boolean verifyCode(String value);

    ContactConfirmationResponse sendSmsReg(String contact) throws JsonProcessingException;

    ContactConfirmationResponse sendEmailReg(String contact);
}

