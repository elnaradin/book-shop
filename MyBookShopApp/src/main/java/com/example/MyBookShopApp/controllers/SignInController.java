package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.dto.SearchWordDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;


@Controller
public class SignInController {

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto(){
        return new SearchWordDto();
    }
    @GetMapping("/signin")
    public String signInPage() {
        return "/signin";
    }
}
