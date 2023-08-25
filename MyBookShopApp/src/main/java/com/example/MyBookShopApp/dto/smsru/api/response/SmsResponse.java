package com.example.MyBookShopApp.dto.smsru.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SmsResponse {
    private String status;
    private String statusText;
    private int code;
    private String callId;
    private double balance;
    private double cost;
    private int freeRepeat;
}