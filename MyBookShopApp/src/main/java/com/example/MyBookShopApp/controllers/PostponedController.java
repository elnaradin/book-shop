package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.dto.BooksWithTotalPrice;
import com.example.MyBookShopApp.dto.SearchWordDto;
import com.example.MyBookShopApp.services.BookStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequiredArgsConstructor
@RequestMapping("/books")
public class PostponedController {
    private final BookStatusService bookStatusService;

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @GetMapping("/postponed")
    public String handlePostponeRequest(Model model) {
        model.addAttribute("postponedBooks", new BooksWithTotalPrice(bookStatusService.getPostponedBooks()));
        model.addAttribute("postponedPresent", bookStatusService.areTherePostponed());
        return "postponed";
    }

    @PostMapping("/changeBookStatus/postponed/remove/{slug}")
    public String handleRemoveBookFromPostponedRequest(@PathVariable String slug, Model model) {
        bookStatusService.removeBookFromPostponed(slug);
        model.addAttribute("postponedPresent", bookStatusService.areTherePostponed());
        return "redirect:/books/postponed";
    }

    @PostMapping("/changeBookStatus/postponed/{slug}")
    public String postponeBook(@PathVariable String slug,
                               Model model) {
        model.addAttribute("postponedPresent", bookStatusService.areTherePostponed());
        bookStatusService.postponeBook(slug);
        return "redirect:/books/" + slug;
    }

}
