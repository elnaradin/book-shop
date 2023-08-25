package com.example.MyBookShopApp.dto.payment.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionsDto {
    private Integer count;
    private List<TransactionDto> transactions;
}
