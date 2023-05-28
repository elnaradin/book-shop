package com.example.MyBookShopApp.model.enums;

public enum UserRoles {
    ADMIN(2), ANONYMOUS(3), USER(1);

    private final Integer id;

    UserRoles(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
