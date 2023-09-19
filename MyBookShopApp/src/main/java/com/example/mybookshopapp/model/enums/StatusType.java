package com.example.mybookshopapp.model.enums;

import lombok.Getter;

@Getter
public enum StatusType {
    KEPT("kept"),
    CART("cart"),
    PAID(""),
    ARCHIVED(""),
    UNLINK(""),
    RECENTLY_VIEWED("");

    private final String cookieName;

    StatusType(String cookieName) {
        this.cookieName = cookieName;
    }
}
