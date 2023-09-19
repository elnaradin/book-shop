package com.example.mybookshopapp.controllers.shop;

import com.example.mybookshopapp.services.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class FaqController {

    private final ShopService shopService;
    @GetMapping("/faq")
    public String faqPage(Model model) {
        model.addAttribute("faqs", shopService.getAllFaqs());
        return "faq";
    }
}
