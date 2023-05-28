package com.example.MyBookShopApp.errs;

public class EmptySearchException extends RuntimeException {
    public EmptySearchException(String message) {
        super(message);
    }
}
