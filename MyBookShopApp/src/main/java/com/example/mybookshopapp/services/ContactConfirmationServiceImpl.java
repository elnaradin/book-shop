package com.example.mybookshopapp.services;

import com.example.mybookshopapp.config.security.IAuthenticationFacade;
import com.example.mybookshopapp.dto.security.ContactConfirmationPayload;
import com.example.mybookshopapp.dto.security.ContactConfirmationResponse;
import com.example.mybookshopapp.dto.smsru.api.response.SmsResponse;
import com.example.mybookshopapp.model.enums.ContactType;
import com.example.mybookshopapp.model.user.UserContactEntity;
import com.example.mybookshopapp.model.user.UserEntity;
import com.example.mybookshopapp.repositories.UserContactRepository;
import com.example.mybookshopapp.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import static com.example.mybookshopapp.errs.Messages.getMessageForLocale;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactConfirmationServiceImpl implements ContactConfirmationService {
    @Value("${smsru.apiId}")
    private String apiId;
    private final RestTemplate restTemplate;
    private final UserContactRepository codeRepository;
    private final JavaMailSender emailSender;
    private final ObjectMapper objectMapper;
    @Value("${EMAIL_USERNAME}")
    private String emailUserName;
    private final UserRepository userRepository;
    @Value("${code-expiration-min}")
    private Integer expireSeconds;
    private final IAuthenticationFacade facade;
    @Value("${contact-confirmation-max-trials}")
    private Integer maxTrials;
    private final PasswordEncoder passwordEncoder;
    private final Random rand = new SecureRandom();

    @Transactional
    public ContactConfirmationResponse sendLoginCall(String contact) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        if (!userRepository.existsByPhone(contact)) {
            response.setError(getMessageForLocale("error.phoneNotFound"));
            return response;
        }
        return sendCall(contact, response);
    }

    @Override
    public ContactConfirmationResponse sendRegCall(String contact) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        if (userRepository.existsByPhone(contact)) {
            response.setError(getMessageForLocale("error.phoneExists"));
            return response;
        }
        return sendCall(contact, response);
    }

    @Override
    public ContactConfirmationResponse sendLoginEmail(String contact) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        if (!userRepository.existsByEmail(contact)) {
            response.setError(getMessageForLocale("error.emailNotFound"));
            return response;
        }
        return sendEmail(contact, response);
    }

    @Override
    public ContactConfirmationResponse sendRegEmail(String contact) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        if (userRepository.existsByEmail(contact)) {
            response.setError(getMessageForLocale("error.emailExists"));
            return response;
        }
        return sendEmail(contact, response);
    }

    private ContactConfirmationResponse sendCall(String phone, ContactConfirmationResponse response) {
        Optional<UserContactEntity> contact = codeRepository.findByContactAndCodeTimeAfter(phone, LocalDateTime.now());
        if (contact.isPresent()) {
            response.setError(getMessageForLocale("error.timeIsNotUp") + getRemainingTime(contact.get()));
            return response;
        }
        String formattedPhone = phone.replace("+", "");
        String url = "https://sms.ru/code/call" +
                "?phone=" + formattedPhone +
                "&ip=33.22.11.55" +
                "&api_id=" + apiId;
        try {
            String smsResponseString = restTemplate.getForEntity(url, String.class).getBody();
            SmsResponse smsResponse = objectMapper.readValue(smsResponseString, SmsResponse.class);
            String code = String.valueOf(Objects.requireNonNull(smsResponse).getCode());
            UserContactEntity newContact = new UserContactEntity(code, expireSeconds, facade.getPrincipal(), phone, ContactType.PHONE);
            if (smsResponse.getStatus().equals("ERROR")) {
                response.setError(smsResponse.getStatusText());
            } else {
                codeRepository.save(newContact);
                response.setResult("true");
            }
        } catch (Exception e) {
            response.setError(getMessageForLocale("error.phoneCallNotExecuted"));
        }

        return response;
    }


    private ContactConfirmationResponse sendEmail(String to, ContactConfirmationResponse response) {
        Optional<UserContactEntity> contact = codeRepository.findByContactAndCodeTimeAfter(to, LocalDateTime.now());
        if (contact.isPresent()) {
            response.setError(getMessageForLocale("error.timeIsNotUp") + getRemainingTime(contact.get()));
            return response;
        }
        String code = generateEmailCode();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailUserName);
        message.setTo(to);
        message.setSubject(getMessageForLocale("email.subject"));
        message.setText(getMessageForLocale("email.codeMessage") + code);
        try {
            emailSender.send(message);
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setError(getMessageForLocale("error.invalidEmail"));
            return response;
        }
        UserContactEntity newContact = new UserContactEntity(code, expireSeconds, facade.getPrincipal(), to, ContactType.EMAIL);
        codeRepository.save(newContact);
        response.setResult("true");
        return response;
    }

    private String generateEmailCode() {
        int number = this.rand.nextInt(999999);
        return String.format("%06d", number);
    }

    @Override
    public ContactConfirmationResponse verifyPassword(ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        Optional<UserEntity> user = userRepository.findByEmail(payload.getContact());
        if (user.isEmpty() || user.get().getPassword() == null) {
            response.setError(getMessageForLocale("error.wrongEmailOrPass"));
            return response;
        }
        UserContactEntity contact = codeRepository.findByContactAndCodeTimeAfter(payload.getContact(), LocalDateTime.now())
                .orElse(new UserContactEntity(user.get().getPassword(), expireSeconds, user.get(), payload.getContact(), ContactType.EMAIL));
        if (contact.getCodeTrails() + 1 >= maxTrials) {
            response.setError(getMessageForLocale("error.trialsNumberExceeded") + getRemainingTime(contact));
            return response;
        } else if (!passwordEncoder.matches(payload.getCode(), contact.getCode())) {
            int trials = contact.getCodeTrails() + 1;
            contact.setCodeTrails(trials);
            codeRepository.save(contact);
            response.setError(getMessageForLocale("error.wrongPass") + (maxTrials - trials));
            return response;
        }
        contact.setApproved(1);
        codeRepository.save(contact);
        response.setResult("true");
        return response;
    }


    @Override
    public ContactConfirmationResponse verifyCode(ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        String formattedValue = payload.getCode().replaceAll("\\s", "");
        Optional<UserContactEntity> contactOpt = codeRepository.findByContactAndCodeTimeAfter(payload.getContact(), LocalDateTime.now());
        if (contactOpt.isEmpty()) {
            response.setError(getMessageForLocale("error.wrongCode"));
            return response;
        }
        UserContactEntity contact = contactOpt.get();
        if (contact.getCodeTrails() + 1 >= maxTrials) {
            response.setError(getMessageForLocale("error.trialsNumberExceeded") + getRemainingTime(contact));
            return response;
        } else if (!contact.getCode().equals(formattedValue)) {
            int trials = contact.getCodeTrails() + 1;
            contact.setCodeTrails(trials);
            codeRepository.save(contact);
            response.setError(getMessageForLocale("error.wrongCodeWithAttempts") + (maxTrials - trials));
            return response;
        }
        contact.setApproved(1);
        codeRepository.save(contact);
        response.setResult("true");
        return response;
    }

    private static String getRemainingTime(UserContactEntity entity) {
        long minutes = ChronoUnit.MINUTES.between(LocalDateTime.now(), entity.getCodeTime());
        long seconds = ChronoUnit.SECONDS.between(LocalDateTime.now(), entity.getCodeTime()) - minutes * 60;
        return getMessageForLocale("error.tryLater") + minutes + " "
                + getMessageForLocale("time.min")+" " + seconds + " "
                + getMessageForLocale("time.sec");
    }


}



