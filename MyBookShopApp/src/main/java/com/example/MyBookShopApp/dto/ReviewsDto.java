package com.example.MyBookShopApp.dto;

import com.example.MyBookShopApp.model.book.review.BookReviewEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ReviewsDto implements Comparable<ReviewsDto>{
    private Integer id;
    private String userName;
    private LocalDateTime time;
    private String text;
    private Integer likeValue;
    private Integer dislikeValue;

    public ReviewsDto(BookReviewEntity review, Integer likeValue, Integer dislikeValue) {
        id = review.getId();
        userName = review.getUser().getName();
        time = review.getTime();
        text = review.getText();
        this.likeValue = likeValue;
        this.dislikeValue = dislikeValue;
    }

    @Override
    public int compareTo(ReviewsDto o) {
        int value1 = this.likeValue - this.dislikeValue;
        int value2 = o.likeValue - o.dislikeValue;
        return Integer.compare(value1, value2);
    }
}
