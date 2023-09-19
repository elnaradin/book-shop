package com.example.mybookshopapp.services;

import com.example.mybookshopapp.dto.security.ContactConfirmationPayload;
import com.example.mybookshopapp.dto.security.ContactConfirmationResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface ContactConfirmationService {
    ContactConfirmationResponse sendLoginCall(String contact) throws JsonProcessingException;

    ContactConfirmationResponse sendLoginEmail(String contact);

    ContactConfirmationResponse verifyPassword(ContactConfirmationPayload payload);

    ContactConfirmationResponse verifyCode(ContactConfirmationPayload value);

    ContactConfirmationResponse sendRegCall(String contact) throws JsonProcessingException;

    ContactConfirmationResponse sendRegEmail(String contact);

}

