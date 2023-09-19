package com.example.mybookshopapp.dto.payment.yookassa.response;

import com.example.mybookshopapp.dto.payment.yookassa.Amount;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@ToString
public class RootResponse {
    private String id;
    private String status;
    private Amount amount;
    private String description;
    private Recipient recipient;
    private Date createdAt;
    private Confirmation confirmation;
    private boolean test;
    private boolean paid;
    private boolean refundable;

}
