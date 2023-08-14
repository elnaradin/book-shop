package com.example.MyBookShopApp.model.enums;

import lombok.Getter;

@Getter
public enum StatusType {
    KEPT("kept"),
    CART("cart"),
    PAID(""),
    ARCHIVED(""),
    UNLINK("");

    private final String cookieName;
    StatusType(String cookieName) {
        this.cookieName = cookieName;
    }
}
