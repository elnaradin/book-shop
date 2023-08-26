package com.example.MyBookShopApp.services.payment;

import com.example.MyBookShopApp.dto.ResultDto;
import com.example.MyBookShopApp.dto.payment.PaymentDto;
import com.example.MyBookShopApp.dto.payment.transaction.TransactionsDto;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.net.URISyntaxException;

public interface PaymentService {


    @Transactional
    String getPaymentUrl(PaymentDto paymentDto) throws URISyntaxException;

    @Transactional
    ResultDto buyCartItems();

    TransactionsDto getTransactions(Pageable pageable);

    boolean checkIfPaymentSucceeded(String userEmail);
}
