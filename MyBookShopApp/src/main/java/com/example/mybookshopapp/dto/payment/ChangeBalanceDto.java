package com.example.mybookshopapp.dto.payment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangeBalanceDto {
    private String username;
    private Integer amount;
    private OperationType operationType;
}
