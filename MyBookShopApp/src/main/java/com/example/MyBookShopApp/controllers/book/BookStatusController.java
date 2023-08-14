package com.example.MyBookShopApp.controllers.book;

import com.example.MyBookShopApp.errs.UserNotAuthenticatedException;
import com.example.MyBookShopApp.model.enums.StatusType;
import com.example.MyBookShopApp.services.bookStatus.BookStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
@RequiredArgsConstructor
public class BookStatusController {
    private final BookStatusService statusService;
    @GetMapping("/postponed")
    public String handlePostponeRequest(Model model, Authentication authentication) {
        model.addAttribute("booksPage", statusService.getBooksByStatus(StatusType.KEPT, authentication));
        return "postponed";
    }
    @GetMapping("/anonym/postponed")
    public String handleAnonymRequest(Model model) {
        model.addAttribute("booksPage", statusService.getAnonymUserBooks(StatusType.KEPT));
        return "postponed";
    }
    @GetMapping("/cart")
    public String handleCartRequest(Model model, Authentication authentication) {

        model.addAttribute("booksPage", statusService.getBooksByStatus(StatusType.CART, authentication));
        return "cart";
    }
    @GetMapping("/anonym/cart")
    public String handleAnonymCartRequest(Model model) {

        model.addAttribute("booksPage", statusService.getAnonymUserBooks(StatusType.CART));
        return "cart";
    }

    @GetMapping("/my")
    public String handleMy(Model model, Authentication authentication) {
        model.addAttribute("booksPage", statusService.getBooksByStatus(StatusType.PAID, authentication));
        return "my";
    }

    @GetMapping("/myarchive")
    public String handleMyArchive(Model model, Authentication authentication)
            throws UserNotAuthenticatedException {
        model.addAttribute("booksPage", statusService.getBooksByStatus(StatusType.ARCHIVED, authentication));
        return "myarchive";
    }
}
