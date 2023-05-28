package com.example.MyBookShopApp.controllers.user;

import com.example.MyBookShopApp.dto.book.BooksPageDto;
import com.example.MyBookShopApp.dto.security.ContactConfirmationPayload;
import com.example.MyBookShopApp.dto.security.ContactConfirmationResponse;
import com.example.MyBookShopApp.dto.security.RegistrationForm;
import com.example.MyBookShopApp.errs.UserNotAuthenticatedException;
import com.example.MyBookShopApp.model.enums.StatusType;
import com.example.MyBookShopApp.services.BookService;
import com.example.MyBookShopApp.services.UserService;
import com.example.MyBookShopApp.services.security.BookstoreUserRegister;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthUserController {

    private final BookstoreUserRegister userRegister;
    private final BookService bookService;
    private final UserService userService;


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
    public String handleUserRegistration(RegistrationForm registrationForm, Model model) {
        userRegister.registerNewUser(registrationForm, model);
        return "signin";
    }

    @PostMapping("/login")
    @ResponseBody
    public ContactConfirmationResponse handleLogin(@RequestBody ContactConfirmationPayload payload)
            throws UserNotAuthenticatedException {
        return userRegister.jwtLogin(payload);
    }

    @GetMapping("/my")
    public String handleMy(Model model) {
        showAuthUsersBooks(model, StatusType.PAID);
        return "my";
    }

    private void showAuthUsersBooks(Model model, StatusType statusType) {
        BooksPageDto pageOfMyBooks = bookService.getPageOfMyBooks(statusType);
        model.addAttribute("books", pageOfMyBooks);
    }


    @GetMapping("/myarchive")
    public String handleMyArchive(Model model)
            throws UserNotAuthenticatedException {
        showAuthUsersBooks(model, StatusType.ARCHIVED);
        return "myarchive";
    }

    @GetMapping("/profile")
    public String handleProfile(Model model) {
        model.addAttribute("curUsr", userService.getCurUser());
        return "profile";
    }


    @GetMapping("/books/cart")
    public String handleCartRequest(Model model) {
        userService.showListOfBooksSelectedByUser(StatusType.CART, model);
        return "cart";
    }

    @GetMapping("/books/postponed")
    public String handlePostponeRequest(Model model) {
        userService.showListOfBooksSelectedByUser(StatusType.KEPT, model);
        return "postponed";
    }

}
