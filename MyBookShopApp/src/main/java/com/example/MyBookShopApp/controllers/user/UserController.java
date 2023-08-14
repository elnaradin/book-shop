package com.example.MyBookShopApp.controllers.user;

import com.example.MyBookShopApp.config.security.jwt.JWTUtils;
import com.example.MyBookShopApp.dto.security.ContactConfirmationPayload;
import com.example.MyBookShopApp.dto.security.ContactConfirmationResponse;
import com.example.MyBookShopApp.dto.security.RegistrationForm;
import com.example.MyBookShopApp.repositories.JwtBlackListRepository;
import com.example.MyBookShopApp.services.book.BookService;
import com.example.MyBookShopApp.services.loginAndRegistration.UserRegService;
import com.example.MyBookShopApp.services.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {


    private final BookService bookService;
    private final UserRegService userService;
    private final JwtBlackListRepository blackListRepository;
    private final CookieUtils cookieUtils;
    private final JWTUtils jwtUtil;


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
    public ContactConfirmationResponse handleRequestContactConfirmation(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    @PostMapping("/approveContact")
    @ResponseBody
    public ContactConfirmationResponse handleApproveContact(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    @PostMapping("/reg")
    public String handleUserRegistration(RegistrationForm registrationForm, RedirectAttributes model) {
        userService.registerNewUser(registrationForm);
        model.addFlashAttribute("regOk", true);
        return "redirect:/signin";
    }

    @PostMapping("/login")
    @ResponseBody
    public ContactConfirmationResponse handleLogin(@RequestBody ContactConfirmationPayload payload,
                                                   HttpServletResponse response) {
        response.addCookie(new Cookie("token", userService.jwtLogin(payload).getResult()));
        return userService.jwtLogin(payload);
    }

    @GetMapping("/profile")
    public String handleProfile(Model model) {

        return "profile";
    }
}
