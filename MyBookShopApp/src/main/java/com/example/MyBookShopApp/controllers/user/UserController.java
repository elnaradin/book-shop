package com.example.MyBookShopApp.controllers.user;

import com.example.MyBookShopApp.annotation.RequestParamsTrackable;
import com.example.MyBookShopApp.dto.ResultDto;
import com.example.MyBookShopApp.dto.book.request.RequestDto;
import com.example.MyBookShopApp.dto.payment.PaymentDto;
import com.example.MyBookShopApp.dto.payment.transaction.TransactionsDto;
import com.example.MyBookShopApp.dto.payment.yookassa.request.ProfileUpdateDto;
import com.example.MyBookShopApp.dto.security.ContactConfirmationPayload;
import com.example.MyBookShopApp.dto.security.ContactConfirmationResponse;
import com.example.MyBookShopApp.dto.security.RegistrationForm;
import com.example.MyBookShopApp.services.contactConfirmation.ContactConfirmationService;
import com.example.MyBookShopApp.services.loginAndRegistration.UserRegService;
import com.example.MyBookShopApp.services.payment.PaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
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
    public ContactConfirmationResponse handleRequestContactConfirmation(@RequestBody ContactConfirmationPayload payload) throws JsonProcessingException {
        return contactConfirmationService.sendSmsLogin(payload.getContact());
    }

    @PostMapping("/requestEmailConfirmation")
    @ResponseBody
    @RequestParamsTrackable
    public ContactConfirmationResponse handleRequestEmailConfirmation(@RequestBody ContactConfirmationPayload payload) {
        return contactConfirmationService.sendEmailLogin(payload.getContact());
    }

    @PostMapping("/reg/requestContactConfirmation")
    @ResponseBody
    @RequestParamsTrackable
    public ContactConfirmationResponse handleRegRequestContactConfirmation(@RequestBody ContactConfirmationPayload payload)
            throws JsonProcessingException {
        return contactConfirmationService.sendSmsReg(payload.getContact());
    }

    @PostMapping("/reg/requestEmailConfirmation")
    @ResponseBody
    @RequestParamsTrackable
    public ContactConfirmationResponse handleRegRequestEmailConfirmation(@RequestBody ContactConfirmationPayload payload) {
        return contactConfirmationService.sendEmailReg(payload.getContact());
    }

    @PostMapping("/approveContact")
    @ResponseBody
    @RequestParamsTrackable
    public ContactConfirmationResponse handleApproveContact(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        if (contactConfirmationService.verifyCode(payload.getCode())) {
            response.setResult("true");
        }
        return response;
    }

    @PostMapping("/reg")
    public String handleUserRegistration(@Valid RegistrationForm registrationForm) {
        userService.registerNewUser(registrationForm);
        return "redirect:/profile";
    }

    @PostMapping("/login-by-email")
    @RequestParamsTrackable
    @ResponseBody
    public ContactConfirmationResponse handleLoginByEmail(
            @RequestBody ContactConfirmationPayload payload,
            HttpServletResponse response
    ) {
        if (contactConfirmationService.verifyCode(payload.getCode())) {
            ContactConfirmationResponse contactConfirmationResponse = userService.jwtLoginByEmail(payload);
            response.addCookie(new Cookie("token", contactConfirmationResponse.getResult()));
            return contactConfirmationResponse;
        } else {
            return null;
        }
    }

    @PostMapping("/login-by-phone-number")
    @RequestParamsTrackable
    @ResponseBody
    public ContactConfirmationResponse handleLoginByPhoneNumber(
            @RequestBody ContactConfirmationPayload payload,
            HttpServletResponse response
    ) {
        if (contactConfirmationService.verifyCode(payload.getCode())) {
            ContactConfirmationResponse contactConfirmationResponse = userService.jwtLoginByPhone(payload);
            response.addCookie(new Cookie("token", contactConfirmationResponse.getResult()));
            return contactConfirmationResponse;
        } else {
            return null;
        }
    }

    @PostMapping("/login-by-pass")
    @RequestParamsTrackable
    @ResponseBody
    public ContactConfirmationResponse handleLoginByPassword(
            @RequestBody ContactConfirmationPayload payload,
            HttpServletResponse response
    ) {
        ContactConfirmationResponse contactConfirmationResponse = userService.jwtLogin(payload);
        if (contactConfirmationResponse != null) {
            response.addCookie(new Cookie("token", contactConfirmationResponse.getResult()));
        }
        return contactConfirmationResponse;
    }

    @GetMapping("/profile")
    public String handleProfile(Model model) {
        model.addAttribute(
                "transactionHistory",
                paymentService.getTransactions( PageRequest.of(
                        0, 4, Sort.by(Sort.Direction.DESC, "time")))
        );
        return "profile";
    }

    @GetMapping("/api/payment/check")
    public String handlePaymentSuccess(
            @RequestParam(value = "isPaid", required = false) Boolean isPaid,
            RedirectAttributes model,
            Authentication authentication
    ) {
        if (isPaid != null && isPaid) {
            model.addFlashAttribute(
                    "isPaid",
                    paymentService.checkIfPaymentSucceeded(authentication.getName())
            );
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
