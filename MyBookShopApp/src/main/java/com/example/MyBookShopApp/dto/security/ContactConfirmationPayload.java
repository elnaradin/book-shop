package com.example.MyBookShopApp.dto.security;

import lombok.Data;

@Data
public class ContactConfirmationPayload {
    private String contact;
    private String code;

    public String getContact() {
        if (contact.contains("@")) {
            return contact;
        }
        return contact.replaceAll("[^+0-9]", "");
    }
}
