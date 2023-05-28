package com.example.MyBookShopApp.controllers.shop;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ContactsController {

    @GetMapping("/contacts")
    public String recentBooksPage() {
        return "contacts";
    }
}
