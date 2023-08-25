package com.example.MyBookShopApp.dto.payment.yookassa.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Confirmation {
    private String type;
    private String confirmationUrl;
}
