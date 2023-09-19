package com.example.mybookshopapp.dto.review;

import java.time.LocalDateTime;


public interface ReviewDto {
    String getHash();

    String getUserName();

    LocalDateTime getTime();

    String getText();

    Integer getRating();

    Long getLikeCount();

    Long getDislikeCount();

    Boolean getIsLiked();

    Boolean getIsDisliked();


}
