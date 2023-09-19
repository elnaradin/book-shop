package com.example.mybookshopapp.controllers.shop;

import com.example.mybookshopapp.services.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class DocumentsController {
    private final ShopService shopService;
    @GetMapping("/documents")
    public String documentsPage(Model model) {
        model.addAttribute("documents",  shopService.getAllDocuments());
        return "/documents/index";
    }

    @GetMapping("/documents/{slug}")
    public String documentPage(Model model, @PathVariable String slug) {
        model.addAttribute("document", shopService.getDocumentBySlug(slug));
        return "/documents/slug";
    }
}
