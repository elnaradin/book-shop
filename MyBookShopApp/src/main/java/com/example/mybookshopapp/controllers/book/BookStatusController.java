package com.example.mybookshopapp.controllers.book;

import com.example.mybookshopapp.model.enums.StatusType;
import com.example.mybookshopapp.services.BookStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class BookStatusController {
    private final BookStatusService statusService;
    private static final String BOOK_PAGE_ATTRIBUTE = "booksPage";

    @GetMapping("/postponed")
    public String handlePostponeRequest(Model model) {
        model.addAttribute(BOOK_PAGE_ATTRIBUTE, statusService.getBooksByStatus(StatusType.KEPT));
        return "postponed";
    }

    @GetMapping("/anonym/postponed")
    public String handleAnonymRequest(Model model) {
        model.addAttribute(BOOK_PAGE_ATTRIBUTE, statusService.getAnonymUserBooks(StatusType.KEPT));
        return "postponed";
    }

    @GetMapping("/cart")
    public String handleCartRequest(Model model) {
        model.addAttribute(BOOK_PAGE_ATTRIBUTE, statusService.getBooksByStatus(StatusType.CART));
        return "cart";
    }

    @GetMapping("/anonym/cart")
    public String handleAnonymCartRequest(Model model) {

        model.addAttribute(BOOK_PAGE_ATTRIBUTE, statusService.getAnonymUserBooks(StatusType.CART));
        return "cart";
    }

    @GetMapping("/my")
    public String handleMy(Model model) {
        model.addAttribute(BOOK_PAGE_ATTRIBUTE, statusService.getBooksByStatus(StatusType.PAID));
        return "my";
    }

    @GetMapping("/myarchive")
    public String handleMyArchive(Model model) {
        model.addAttribute(BOOK_PAGE_ATTRIBUTE, statusService.getBooksByStatus(StatusType.ARCHIVED));
        return "myarchive";
    }
    @GetMapping("/recentlyViewed")
    public String recentlyViewed(Model model) {
        model.addAttribute(BOOK_PAGE_ATTRIBUTE, statusService.getBooksByStatus(StatusType.RECENTLY_VIEWED));
        return "/books/recentlyviewed";
    }
}
