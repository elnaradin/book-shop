package com.example.mybookshopapp.controllers.shop;

import com.example.mybookshopapp.dto.ResultDto;
import com.example.mybookshopapp.dto.shop.MessageDto;
import com.example.mybookshopapp.services.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class ContactsController {
    private final ShopService shopService;

    @GetMapping("/contacts")
    public String contactsPage() {
        return "contacts";
    }
    @PostMapping("/contacts")
    @ResponseBody
    public ResultDto handleContactMessage(@Valid @RequestBody MessageDto message) {
        return shopService.saveMessage(message);
    }
}
