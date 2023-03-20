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
        model.addAttribute("postponedBooks", new BooksWithTotalPrice(bookStatusService.getBooksByStatus(StatusType.KEPT.toString())));
        model.addAttribute("postponedPresent", bookStatusService.booksByStatusExist(StatusType.KEPT.toString()));
        return "postponed";
    }

}
