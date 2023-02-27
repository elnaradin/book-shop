package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.model.book.BookEntity;
import org.springframework.stereotype.Service;

@Service
public class BooksRatingAndPopularityService {

    public Double calculateBookPopularity(BookEntity book) {
        int usersThatPaid = getUserTypeCount(book, "PAID");
        int usersThatHaveInCart = getUserTypeCount(book, "CART");
        int usersThatKept = getUserTypeCount(book, "KEPT");
        return usersThatPaid +
                0.7 * usersThatHaveInCart +
                0.4 * usersThatKept;
    }

    private int getUserTypeCount(BookEntity book, String type) {
        return (int) book.getUsers().stream()
                .filter(b2u -> b2u.getType().getName().equals(type))
                .count();
    }
}
