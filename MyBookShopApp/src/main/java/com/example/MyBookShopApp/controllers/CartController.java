package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.dto.BooksWithTotalPrice;
import com.example.MyBookShopApp.dto.SearchWordDto;
import com.example.MyBookShopApp.model.book.BookEntity;
import com.example.MyBookShopApp.repositories.BookRepository;
import com.example.MyBookShopApp.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class CartController {

    private final BookService bookService;
    private final BookRepository bookRepository;
    private final String cookiePath = "/books";
    private final String cookieName = "cartContents";


    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute(name = "bookCart")
    public BooksWithTotalPrice bookCart() {
        return new BooksWithTotalPrice();
    }

    @GetMapping("/cart")
    public String handleCartRequest(@CookieValue(value = "cartContents", required = false) String cartContents,
                                    Model model) {
        if (cartContents == null || cartContents.equals("")) {
            model.addAttribute("isCartEmpty", true);
        } else {
            model.addAttribute("isCartEmpty", false);
            cartContents = cartContents.startsWith("/") ? cartContents.substring(1) : cartContents;
            cartContents = cartContents.endsWith("/") ? cartContents.substring(0, cartContents.length() - 1) : cartContents;
            String[] cookieSlugs = cartContents.split("/");
            List<BookEntity> booksFromCookieSlugs = bookRepository.findBookEntitiesBySlugIn(cookieSlugs);
            model.addAttribute("bookCart", new BooksWithTotalPrice(bookService.createBookList(booksFromCookieSlugs)));
        }
        return "cart";
    }

    @PostMapping("/changeBookStatus/cart/remove/{slug}")
    public String handleRemoveBookFromCartRequest(@PathVariable String slug,
                                                  @CookieValue(name = "cartContents", required = false) String cartContents,
                                                  HttpServletResponse response, Model model) {

        if (cartContents != null && !cartContents.equals("")) {
            List<String> cookieBooks = new ArrayList<>(Arrays.asList(cartContents.split("/")));
            cookieBooks.remove(slug);
            Cookie cookie = new Cookie(cookieName, String.join("/", cookieBooks));
            cookie.setPath(cookiePath);
            response.addCookie(cookie);
            model.addAttribute("isCartEmpty", false);
        } else {
            model.addAttribute("isCartEmpty", true);
        }
        return "redirect:/books/cart";
    }

    @PostMapping("/changeBookStatus/cart/{slug}")
    public String handleChangeBookStatus(@PathVariable String slug,
                                         @CookieValue(name = "cartContents", required = false) String cartContents,
                                         HttpServletResponse response,
                                         Model model) {
        if (cartContents == null || cartContents.equals("")) {
            Cookie cookie = new Cookie(cookieName, slug);
            cookie.setPath(cookiePath);
            response.addCookie(cookie);
            model.addAttribute("isCartEmpty", false);
        } else if (!cartContents.contains(slug)) {
            StringJoiner stringJoiner = new StringJoiner("/");
            stringJoiner.add(cartContents).add(slug);
            Cookie cookie = new Cookie(cookieName, stringJoiner.toString());
            cookie.setPath(cookiePath);
            response.addCookie(cookie);
            model.addAttribute("isCartEmpty", false);
        }
        return "redirect:/books/" + slug;

    }
}
