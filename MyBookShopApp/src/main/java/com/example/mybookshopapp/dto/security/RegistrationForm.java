package com.example.mybookshopapp.dto.security;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class RegistrationForm {
    private String name;

    private String email;
    @Pattern(regexp = "^\\+7 \\(\\d{3}\\) \\d{3}-\\d{2}-\\d{2}$")
    private String phone;
    private String pass;
}
