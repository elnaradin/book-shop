package com.example.MyBookShopApp.dto.security;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class RegistrationForm {
    private String name;
    @Pattern(regexp = "^(.+)@(\\S+)$")
    private String email;
    @Pattern(regexp = "^\\+7 \\([0-9]{3}\\) [0-9]{3}-[0-9]{2}-[0-9]{2}$")
    private String phone;
    private String pass;
}
