package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.dto.SearchWordDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class FaqController {

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto(){
        return new SearchWordDto();
    }
    @GetMapping("/faq")
    public String faqPage() {
        return "faq";
    }
}
