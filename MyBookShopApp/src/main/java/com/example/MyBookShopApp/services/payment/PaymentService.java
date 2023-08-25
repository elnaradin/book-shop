package com.example.MyBookShopApp.services.payment;

import com.example.MyBookShopApp.dto.ResultDto;
import com.example.MyBookShopApp.dto.payment.PaymentDto;
import com.example.MyBookShopApp.dto.payment.transaction.TransactionsDto;
import org.springframework.data.domain.Pageable;

import java.net.URISyntaxException;

public interface PaymentService {


    String getPaymentUrl(String name, PaymentDto paymentDto) throws URISyntaxException;

    ResultDto buyCartItems(String name);

    TransactionsDto getTransactions(String name, Pageable pageable);

    boolean checkIfPaymentSucceeded(String userEmail);
}
