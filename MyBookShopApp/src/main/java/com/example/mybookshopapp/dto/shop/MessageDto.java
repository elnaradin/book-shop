package com.example.mybookshopapp.dto.shop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private String name;

    private String mail;
    @NotBlank
    private String topic;
    @NotBlank
    private String message;
}
