package com.example.mybookshopapp.errs;

public class InsufficientFundsException extends LocalizedException {


    public InsufficientFundsException(String messageKey) {
        super(messageKey);
    }
}
