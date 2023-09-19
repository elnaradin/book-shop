package com.example.mybookshopapp.services;

import com.example.mybookshopapp.dto.ResultDto;
import com.example.mybookshopapp.dto.payment.PaymentDto;
import com.example.mybookshopapp.dto.payment.transaction.TransactionsDto;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.net.URISyntaxException;

public interface PaymentService {


    @Transactional
    String getPaymentUrl(PaymentDto paymentDto) throws URISyntaxException;

    @Transactional
    ResultDto buyCartItems();

    TransactionsDto getTransactions(Pageable pageable);

    boolean checkIfPaymentSucceeded();
}
