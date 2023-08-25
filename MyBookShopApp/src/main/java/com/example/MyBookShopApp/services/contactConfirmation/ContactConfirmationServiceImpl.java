package com.example.MyBookShopApp.services.contactConfirmation;

import com.example.MyBookShopApp.dto.security.ContactConfirmationResponse;
import com.example.MyBookShopApp.dto.smsru.api.response.SmsResponse;
import com.example.MyBookShopApp.model.user.ConfirmationCode;
import com.example.MyBookShopApp.repositories.ConfirmationCodeRepository;
import com.example.MyBookShopApp.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ContactConfirmationServiceImpl implements ContactConfirmationService {
    @Value("${smsru.apiId}")
    private String apiId;
    private final RestTemplate restTemplate;
    private final ConfirmationCodeRepository codeRepository;
    private final JavaMailSender emailSender;
    private final ObjectMapper objectMapper;
    @Value("${EMAIL_USERNAME}")
    private String emailUserName;
    private final UserRepository userRepository;
    private final Integer expireSeconds = 120;

    @Transactional
    public ContactConfirmationResponse sendSmsLogin(String phone) throws JsonProcessingException {

        ContactConfirmationResponse response = new ContactConfirmationResponse();
        if (!userRepository.existsByPhone(phone)) {
            return response;
        }
        return sendSms(phone, response);
    }

    private ContactConfirmationResponse sendSms(String phone, ContactConfirmationResponse response) throws JsonProcessingException {
        String formattedPhone = phone.replace("+", "");
        String url = "https://sms.ru/code/call" +
                "?phone=" + formattedPhone +
                "&ip=33.22.11.55" +
                "&api_id=" + apiId;
        String smsResponseString = restTemplate.getForEntity(url, String.class).getBody();
        SmsResponse smsResponse = objectMapper.readValue(smsResponseString, SmsResponse.class);
        String code = String.valueOf(Objects.requireNonNull(smsResponse).getCode());
        ConfirmationCode confirmationCode = new ConfirmationCode(code, expireSeconds);
        if (smsResponse.getStatus().equals("ERROR")) {
            response.setError(smsResponse.getStatusText());
        } else {
            codeRepository.save(confirmationCode);
            response.setResult("true");
        }
        return response;
    }


    @Override
    public ContactConfirmationResponse sendEmailLogin(String to) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        if (!userRepository.existsByEmail(to)) {
            return response;
        }
        return sendEmail(to, response);
    }

    private ContactConfirmationResponse sendEmail(String to, ContactConfirmationResponse response) {
        String code = generateEmailCode();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailUserName);
        message.setTo(to);
        message.setSubject("Подтверждение почты");
        message.setText("Ваш код: " + code);
        try {
            emailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            return response;
        }
        ConfirmationCode confirmationCode = new ConfirmationCode(code, expireSeconds);
        codeRepository.save(confirmationCode);
        response.setResult("true");
        return response;
    }

    private String generateEmailCode() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }


    @Override
    public boolean verifyCode(String value) {
        String formattedValue = value.replaceAll("\\s", "");
        LocalDateTime now = LocalDateTime.now();
        return codeRepository.existsByValueAndExpiryTimeAfter(formattedValue, now);
    }

    @Override
    public ContactConfirmationResponse sendSmsReg(String contact) throws JsonProcessingException {
        ContactConfirmationResponse response = new ContactConfirmationResponse();

        if (userRepository.existsByPhone(contact)) {
            return response;
        }
        return sendSms(contact, response);
    }

    @Override
    public ContactConfirmationResponse sendEmailReg(String contact) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        if (userRepository.existsByEmail(contact)) {
            return response;
        }
        return sendEmail(contact, response);
    }


}



