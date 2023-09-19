package com.example.mybookshopapp.dto.payment;

import lombok.Data;

@Data
public class PaymentDto {
    private String hash;
    private Integer sum;
    private Long time;
}
