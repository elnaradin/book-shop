package com.example.mybookshopapp.dto.payment.yookassa.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Recipient {
    private String accountId;
    private String gatewayId;
}
