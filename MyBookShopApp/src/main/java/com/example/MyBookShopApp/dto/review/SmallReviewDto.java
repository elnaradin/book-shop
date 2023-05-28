package com.example.MyBookShopApp.dto.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmallReviewDto {
    private Integer id;
    private String userName;
    private LocalDateTime time;
    private String text;
}
