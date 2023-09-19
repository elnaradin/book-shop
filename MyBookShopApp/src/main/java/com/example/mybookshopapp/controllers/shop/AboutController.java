package com.example.mybookshopapp.controllers.shop;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AboutController {

    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }
}
