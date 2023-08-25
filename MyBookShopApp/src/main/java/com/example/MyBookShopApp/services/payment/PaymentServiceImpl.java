package com.example.MyBookShopApp.services.payment;


import com.example.MyBookShopApp.dto.ResultDto;
import com.example.MyBookShopApp.dto.book.ChangeStatusPayload;
import com.example.MyBookShopApp.dto.payment.ChangeBalanceDto;
import com.example.MyBookShopApp.dto.payment.OperationType;
import com.example.MyBookShopApp.dto.payment.PaymentDto;
import com.example.MyBookShopApp.dto.payment.transaction.TransactionDto;
import com.example.MyBookShopApp.dto.payment.transaction.TransactionsDto;
import com.example.MyBookShopApp.dto.payment.yookassa.Amount;
import com.example.MyBookShopApp.dto.payment.yookassa.request.Confirmation;
import com.example.MyBookShopApp.dto.payment.yookassa.request.RootRequest;
import com.example.MyBookShopApp.dto.payment.yookassa.response.RootResponse;
import com.example.MyBookShopApp.model.book.BookEntity;
import com.example.MyBookShopApp.model.enums.StatusType;
import com.example.MyBookShopApp.model.payments.BalanceTransactionEntity;
import com.example.MyBookShopApp.model.user.UserEntity;
import com.example.MyBookShopApp.repositories.BalanceTransactionRepository;
import com.example.MyBookShopApp.repositories.BookRepository;
import com.example.MyBookShopApp.repositories.UserRepository;
import com.example.MyBookShopApp.services.bookStatus.BookStatusService;
import com.example.MyBookShopApp.services.loginAndRegistration.UserRegService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final RestTemplate restTemplate;

    private final BookRepository bookRepository;
    private final BookStatusService bookStatusService;


    @Value("${yookassa.shopId}")
    private String shopId;
    @Value("${yookassa.key}")
    private String key;
    private final UserRegService userRegService;
    private final BalanceTransactionRepository balanceTransactionRepository;
    private final UserRepository userRepository;
    private final BalanceTransactionRepository transactionRepository;

    @Override
    @Transactional
    public String getPaymentUrl(String userName, PaymentDto paymentDto) throws URISyntaxException {
        String url = "http://localhost:8085/api/payment/check" +
                "?isPaid=true";
        RootResponse response = sendPaymentRequest(userName, paymentDto.getSum(), url).getBody();

        UserEntity user = userRepository.findByEmail(userName).orElseThrow();
        if (response != null) {
            log.info(response.toString());
            user.setHash(response.getId());
            userRepository.save(user);
        }
        return Objects.requireNonNull(response).getConfirmation().getConfirmationUrl();
    }

    @Override
    @Transactional
    public ResultDto buyCartItems(String userName) {
        List<BookEntity> books = bookRepository.getBookEntitiesByUserAndStatus(userName, StatusType.CART);
        Integer totalPrice = books.stream().mapToInt(PaymentServiceImpl::getDiscountPrice).sum();
        userRegService.changeBalance(
                ChangeBalanceDto.builder()
                        .username(userName)
                        .amount(totalPrice)
                        .operationType(OperationType.WRITE_OFF)
                        .build()
        );
        changeBooksStatus(userName, books);
        UserEntity user = userRepository.findByEmail(userName).orElseThrow();
        for (BookEntity book : books) {
            String description = "Покупка книги \"" + book.getTitle() + "\"";
            createTransaction(user, description, -getDiscountPrice(book), book);
        }
        return ResultDto.builder().result(true).build();
    }

    private static int getDiscountPrice(BookEntity b) {
        return Math.round(b.getPrice() * (1 - ((float) b.getDiscount() / 100)));
    }

    private ResponseEntity<RootResponse> sendPaymentRequest(String userName, Integer totalPrice, String returnUrl) throws URISyntaxException {
        URI uri = new URI("https://api.yookassa.ru/v3/payments");
        Amount amount = new Amount();
        amount.setCurrency("RUB");
        amount.setValue(String.valueOf(totalPrice));
        Confirmation confirmation = new Confirmation();
        confirmation.setReturnUrl(returnUrl);
        confirmation.setType("redirect");
        RootRequest root = new RootRequest();
        root.setAmount(amount);
        root.setCapture(true);
        root.setConfirmation(confirmation);
        root.setDescription("Поступил платеж от пользователя " + userName);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Idempotence-Key", UUID.randomUUID().toString());
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(shopId, key);
        HttpEntity<RootRequest> httpEntity = new HttpEntity<>(root, headers);
        return restTemplate.postForEntity(uri, httpEntity, RootResponse.class);
    }

    @Override
    public TransactionsDto getTransactions(String name, Pageable pageable) {
        Page<TransactionDto> transactions = balanceTransactionRepository.findTransactionsByUserEmail(name, pageable);
        return new TransactionsDto((int) transactions.getTotalElements(), transactions.getContent());
    }

    @Override
    public boolean checkIfPaymentSucceeded(String userEmail) {
        UserEntity user = userRepository.findByEmail(userEmail).orElseThrow();
        if (user.getHash() == null) {
            return false;
        }
        String url = "https://api.yookassa.ru/v3/payments/" + user.getHash();
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(shopId, key);
        RootResponse response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                RootResponse.class
        ).getBody();
        if (response != null) {

            log.info(response.toString());
            if (response.getStatus().equals("pending")) {
                //отправляет запросы то тех пор, пока не получит статус succeeded
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                checkIfPaymentSucceeded(userEmail);
            }
            if (!response.getStatus().equals("succeeded")) {
                return false;
            }
            int roundedSum = Math.round(Float.parseFloat(response.getAmount().getValue()));
            String description = "Пополнение на " + roundedSum + " руб.";
            log.info("top up sum: " + roundedSum);
            userRegService.changeBalance(ChangeBalanceDto.builder()
                    .amount(roundedSum)
                    .operationType(OperationType.TOP_UP).username(userEmail).build());

            createTransaction(user, description, roundedSum, null);
            return true;
        }
        return false;
    }

    private void createTransaction(UserEntity user, String description, Integer value, BookEntity book) {
        BalanceTransactionEntity transaction = new BalanceTransactionEntity();
        transaction.setDescription(description);
        transaction.setUser(user);
        transaction.setTime(LocalDateTime.now());
        transaction.setValue(value);
        transaction.setBook(book);
        transactionRepository.save(transaction);
    }


    private void changeBooksStatus(String userName, List<BookEntity> books) {
        ChangeStatusPayload payload = new ChangeStatusPayload();
        payload.setBookIds(books.stream().map(BookEntity::getSlug).collect(Collectors.toList()));
        payload.setStatus(StatusType.PAID);
        bookStatusService.changeBookStatus(payload, userName);
    }


}
