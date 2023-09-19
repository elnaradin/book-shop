package com.example.mybookshopapp.errs;

public class EmptySearchException extends LocalizedException {

    public EmptySearchException(String messageKey) {
        super(messageKey);
    }
}
