package com.example.mybookshopapp.errs;

public class FileDownloadLimitReached extends LocalizedException {
    public FileDownloadLimitReached(String messageKey) {
        super(messageKey);
    }
}
