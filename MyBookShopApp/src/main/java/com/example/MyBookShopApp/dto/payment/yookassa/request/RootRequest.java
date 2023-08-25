package com.example.MyBookShopApp.dto.payment.yookassa.request;

import com.example.MyBookShopApp.dto.payment.yookassa.Amount;
import lombok.Data;

@Data
public class RootRequest {
    private Amount amount;
    private boolean capture;
    private Confirmation confirmation;
    private String description;
}
