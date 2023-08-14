package com.example.MyBookShopApp.dto.review;

import java.time.LocalDateTime;


public interface ReviewDto {
    Integer getId();

    String getUserName();

    LocalDateTime getTime();

    String getText();

    Integer getRating();

    Long getLikeCount();

    Long getDislikeCount();

    Boolean getIsLiked();

    Boolean getIsDisliked();


}
