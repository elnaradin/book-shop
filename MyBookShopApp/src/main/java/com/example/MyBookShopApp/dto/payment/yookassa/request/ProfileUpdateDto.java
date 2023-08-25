package com.example.MyBookShopApp.dto.payment.yookassa.request;

import lombok.Data;

@Data
public class ProfileUpdateDto {
    private String name;
    private String mail;
    private String phone;
    private String password;
    private String passwordReply;

    public String getContact() {
        return this.phone.replaceAll("[^+0-9]", "");
    }
}
