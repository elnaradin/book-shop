package com.example.mybookshopapp.errs;

public class UserExistsException extends LocalizedException {

    public UserExistsException(String messageKey) {
        super(messageKey);
    }
}
