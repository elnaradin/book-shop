package com.example.mybookshopapp.errs;

public class ItemNotFoundException extends LocalizedException {

    public ItemNotFoundException(String messageKey) {
        super(messageKey);
    }
}
