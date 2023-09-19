package com.example.mybookshopapp.controllers.user;

import com.example.mybookshopapp.annotation.RequestParamsTrackable;
import com.example.mybookshopapp.dto.ResultDto;
import com.example.mybookshopapp.dto.book.request.RequestDto;
import com.example.mybookshopapp.dto.payment.PaymentDto;
import com.example.mybookshopapp.dto.payment.transaction.TransactionsDto;
import com.example.mybookshopapp.dto.payment.yookassa.request.ProfileUpdateDto;
import com.example.mybookshopapp.dto.security.ContactConfirmationPayload;
import com.example.mybookshopapp.dto.security.ContactConfirmationResponse;
import com.example.mybookshopapp.dto.security.RegistrationForm;
import com.example.mybookshopapp.services.ContactConfirmationService;
import com.example.mybookshopapp.services.UserRegService;
import com.example.mybookshopapp.services.PaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.net.URISyntaxException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserRegService userService;

    private final ContactConfirmationService contactConfirmationService;
    private final PaymentService paymentService;


    @GetMapping("/signin")
    public String handleSignIn() {
        return "signin";
    }

    @GetMapping("/signup")
    public String handleSignUp(Model model) {
        model.addAttribute("regForm", new RegistrationForm());
        return "signup";
    }

    @PostMapping("/requestContactConfirmation")
    @ResponseBody
    @RequestParamsTrackable
    public ContactConfirmationResponse handleRequestContactConfirmation(@RequestBody ContactConfirmationPayload payload)
            throws JsonProcessingException {
        return contactConfirmationService.sendLoginCall(payload.getContact());
    }

    @PostMapping("/requestEmailConfirmation")
    @ResponseBody
    @RequestParamsTrackable
    public ContactConfirmationResponse handleRequestEmailConfirmation(@RequestBody ContactConfirmationPayload payload) {
        return contactConfirmationService.sendLoginEmail(payload.getContact());
    }

    @PostMapping("/reg/requestContactConfirmation")
    @ResponseBody
    @RequestParamsTrackable
    public ContactConfirmationResponse handleRegRequestContactConfirmation(@RequestBody ContactConfirmationPayload payload)
            throws JsonProcessingException {
        return contactConfirmationService.sendRegCall(payload.getContact());
    }

    @PostMapping("/reg/requestEmailConfirmation")
    @ResponseBody
    @RequestParamsTrackable
    public ContactConfirmationResponse handleRegRequestEmailConfirmation(@RequestBody ContactConfirmationPayload payload) {
        return contactConfirmationService.sendRegEmail(payload.getContact());
    }

    @PostMapping("/approveContact")
    @ResponseBody
    @RequestParamsTrackable
    public ContactConfirmationResponse handleApproveContact(@RequestBody ContactConfirmationPayload payload) {
        return contactConfirmationService.verifyCode(payload);

    }

    @PostMapping("/reg")
    public String handleUserRegistration(@Valid RegistrationForm registrationForm) {
        userService.registerNewUser(registrationForm);
        return "redirect:/my";
    }

    @PostMapping("/login-by-email")
    @RequestParamsTrackable
    @ResponseBody
    public ContactConfirmationResponse handleLoginByEmail(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse contactConfirmationResponse = contactConfirmationService.verifyCode(payload);
        if (contactConfirmationResponse.getResult() != null) {
            return userService.jwtLoginByEmail(payload);
        }
        return contactConfirmationResponse;
    }

    @PostMapping("/login-by-phone-number")
    @RequestParamsTrackable
    @ResponseBody
    public ContactConfirmationResponse handleLoginByPhoneNumber(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse contactConfirmationResponse = contactConfirmationService.verifyCode(payload);
        if (contactConfirmationResponse.getResult() != null) {
            return userService.jwtLoginByPhone(payload);
        }
        return contactConfirmationResponse;

    }

    @PostMapping("/login-by-pass")
    @RequestParamsTrackable
    @ResponseBody
    public ContactConfirmationResponse handleLoginByPassword(@RequestBody ContactConfirmationPayload payload) {
        return userService.jwtLogin(payload);
    }

    @GetMapping("/profile")
    public String handleProfile(Model model) {
        model.addAttribute(
                "transactionHistory",
                paymentService.getTransactions(PageRequest.of(
                        0, 4, Sort.by(Sort.Direction.DESC, "time")))
        );
        return "profile";
    }

    @GetMapping("/api/payment/check")
    public String handlePaymentSuccess(@RequestParam(value = "isPaid", required = false) Boolean isPaid, RedirectAttributes model) {
        if (isPaid != null && isPaid) {
            model.addFlashAttribute("isPaid", paymentService.checkIfPaymentSucceeded());
        }
        return "redirect:/profile";
    }

    @RequestParamsTrackable
    @PostMapping("/profile")
    @ResponseBody
    public ResultDto changeProfile(@RequestBody ProfileUpdateDto updateDto) {
        return userService.updateProfile(updateDto);
    }

    @RequestParamsTrackable
    @PostMapping("/payment")
    @ResponseBody
    public ResultDto handlePayment(@RequestBody @Valid PaymentDto paymentDto)
            throws URISyntaxException {
        return ResultDto.builder()
                .message(paymentService.getPaymentUrl(paymentDto))
                .build();
    }

    @RequestParamsTrackable
    @GetMapping("/transactions")
    public ResponseEntity<TransactionsDto> getTransactions(RequestDto requestDto) {
        return ResponseEntity.ok().body(paymentService.getTransactions(PageRequest.of(
                        requestDto.getOffset(),
                        requestDto.getLimit(),
                        Sort.by(requestDto.getSort(), "time")
                )
        ));
    }




}
