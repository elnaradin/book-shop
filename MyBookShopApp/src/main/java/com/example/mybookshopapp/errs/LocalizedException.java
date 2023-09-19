package com.example.mybookshopapp.errs;

public class LocalizedException extends RuntimeException{
    private final String messageKey;

    public LocalizedException(String messageKey) {
        this.messageKey = messageKey;
    }


    @Override
    public String getLocalizedMessage() {
        return Messages.getMessageForLocale(messageKey);
    }
}
