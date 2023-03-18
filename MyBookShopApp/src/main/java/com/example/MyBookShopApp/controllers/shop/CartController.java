package com.example.MyBookShopApp.controllers.shop;

import com.example.MyBookShopApp.dto.SearchWordDto;
import com.example.MyBookShopApp.dto.bookCollections.BooksWithTotalPrice;
import com.example.MyBookShopApp.model.enums.StatusType;
import com.example.MyBookShopApp.services.BookStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class CartController {

    private final BookStatusService bookStatusService;


    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute(name = "bookCart")
    public BooksWithTotalPrice bookCart() {
        return new BooksWithTotalPrice();
    }

    @GetMapping("/cart")
    public String handleCartRequest(Model model) {
        model.addAttribute("bookCart", new BooksWithTotalPrice(bookStatusService
                .getBooksByStatus(StatusType.CART.toString())));
        model.addAttribute("isCartEmpty", !bookStatusService
                .booksByStatusExist(StatusType.CART.toString()));
        return "cart";
    }
}
