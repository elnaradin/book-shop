package com.example.mybookshopapp.services;


import com.example.mybookshopapp.config.security.IAuthenticationFacade;
import com.example.mybookshopapp.dto.ResultDto;
import com.example.mybookshopapp.dto.book.status.ChangeBookStatusDto;
import com.example.mybookshopapp.dto.payment.ChangeBalanceDto;
import com.example.mybookshopapp.dto.payment.OperationType;
import com.example.mybookshopapp.dto.payment.PaymentDto;
import com.example.mybookshopapp.dto.payment.transaction.TransactionDto;
import com.example.mybookshopapp.dto.payment.transaction.TransactionsDto;
import com.example.mybookshopapp.dto.payment.yookassa.Amount;
import com.example.mybookshopapp.dto.payment.yookassa.request.Confirmation;
import com.example.mybookshopapp.dto.payment.yookassa.request.RootRequest;
import com.example.mybookshopapp.dto.payment.yookassa.response.RootResponse;
import com.example.mybookshopapp.model.book.BookEntity;
import com.example.mybookshopapp.model.enums.StatusType;
import com.example.mybookshopapp.model.payments.BalanceTransactionEntity;
import com.example.mybookshopapp.model.user.UserEntity;
import com.example.mybookshopapp.repositories.BalanceTransactionRepository;
import com.example.mybookshopapp.repositories.BookRepository;
import com.example.mybookshopapp.repositories.UserRepository;
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

import static com.example.mybookshopapp.errs.Messages.getMessageForLocale;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PaymentServiceImpl implements PaymentService {
    private final RestTemplate restTemplate;

    private final BookRepository bookRepository;
    private final BookStatusService bookStatusService;


    @Value("${yookassa.shopId}")
    private String shopId;
    @Value("${yookassa.key}")
    private String key;
    @Value("${payment.uri}")
    private String paymentUri;
    private final UserRegService userRegService;
    private final BalanceTransactionRepository balanceTransactionRepository;
    private final UserRepository userRepository;
    private final BalanceTransactionRepository transactionRepository;
    private final IAuthenticationFacade facade;

    @Override
    public String getPaymentUrl(PaymentDto paymentDto) throws URISyntaxException {
        String username = facade.getCurrentUsername();
        String url = "http://localhost:8085/api/payment/check" +
                "?isPaid=true";
        RootResponse response = sendPaymentRequest(username, paymentDto.getSum(), url).getBody();

        UserEntity user = userRepository.findByEmail(username).orElseThrow();
        if (response != null) {
            log.info(response.toString());
            user.setHash(response.getId());
            userRepository.save(user);
        }
        return Objects.requireNonNull(response).getConfirmation().getConfirmationUrl();
    }

    @Override
    public ResultDto buyCartItems() {
        String username = facade.getCurrentUsername();
        List<BookEntity> books = bookRepository.getBookEntitiesByUserAndStatus(username, StatusType.CART);
        Integer totalPrice = books.stream().mapToInt(PaymentServiceImpl::getDiscountPrice).sum();
        userRegService.changeBalance(
                ChangeBalanceDto.builder()
                        .username(username)
                        .amount(totalPrice)
                        .operationType(OperationType.WRITE_OFF)
                        .build()
        );
        changeBooksStatus(books);
        UserEntity user = userRepository.findByEmail(username).orElseThrow();
        for (BookEntity book : books) {
            String description = getMessageForLocale("payment.description") + book.getTitle() + "\"";
            createTransaction(user, description, -getDiscountPrice(book), book);
        }
        return ResultDto.builder().result(true).build();
    }

    private static int getDiscountPrice(BookEntity b) {
        return Math.round(b.getPrice() * (1 - ((float) b.getDiscount() / 100)));
    }

    private ResponseEntity<RootResponse> sendPaymentRequest(String userName, Integer totalPrice, String returnUrl) throws URISyntaxException {
        URI uri = new URI(this.paymentUri);
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
        root.setDescription(getMessageForLocale("payment.sumReceivedFrom") + userName);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Idempotence-Key", UUID.randomUUID().toString());
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(shopId, key);
        HttpEntity<RootRequest> httpEntity = new HttpEntity<>(root, headers);
        return restTemplate.postForEntity(uri, httpEntity, RootResponse.class);
    }

    @Override
    public TransactionsDto getTransactions(Pageable pageable) {
        Page<TransactionDto> transactions = balanceTransactionRepository
                .findTransactionsByUserEmail(facade.getCurrentUsername(), pageable);
        return new TransactionsDto((int) transactions.getTotalElements(), transactions.getContent());
    }

    @Override
    public boolean checkIfPaymentSucceeded() {
        UserEntity user = userRepository.findByEmail(facade.getCurrentUsername()).orElseThrow();
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
            return checkStatusAndCreateTransaction(user, response);
        }
        return false;
    }

    private boolean checkStatusAndCreateTransaction(UserEntity user, RootResponse response) {
        if (response.getStatus().equals("pending")) {
            //отправляет запросы то тех пор, пока не получит статус succeeded. Заглушка
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                Thread.currentThread().interrupt();
            }
            checkIfPaymentSucceeded();
        }
        if (!response.getStatus().equals("succeeded")) {
            return false;
        }
        int roundedSum = Math.round(Float.parseFloat(response.getAmount().getValue()));
        String description = getMessageForLocale("payment.topup") + roundedSum + " "+getMessageForLocale("currency.rub");
        userRegService.changeBalance(ChangeBalanceDto.builder()
                .amount(roundedSum)
                .operationType(OperationType.TOP_UP).username(facade.getCurrentUsername()).build());
        createTransaction(user, description, roundedSum, null);
        return true;
    }

    private void createTransaction(UserEntity user, String description, Integer value, BookEntity book) {
        BalanceTransactionEntity transaction = new BalanceTransactionEntity();
        transaction.setDescription(description);
        transaction.setUser(user);
        transaction.setTime(LocalDateTime.now());
        transaction.setValue(value);
        transaction.setBook(book);
        transactionRepository.saveAndFlush(transaction);
    }


    private void changeBooksStatus(List<BookEntity> books) {
        ChangeBookStatusDto payload = new ChangeBookStatusDto();
        payload.setBookIds(books.stream().map(BookEntity::getSlug).collect(Collectors.toList()));
        payload.setStatus(StatusType.PAID);
        bookStatusService.changeBookStatus(payload);
    }


}
