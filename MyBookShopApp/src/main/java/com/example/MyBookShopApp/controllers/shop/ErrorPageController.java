package com.example.MyBookShopApp.controllers.shop;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorPageController {
    @GetMapping("/404")
    public String handleNotFound() {
        return "error";
    }
}
