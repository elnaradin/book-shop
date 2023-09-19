package com.example.mybookshopapp.dto.admin;

import com.example.mybookshopapp.model.enums.StatusType;
import lombok.Data;

@Data
public class AdminChangeBookStatusDto {
    private String userId;
    private String bookId;
    private StatusType status;
}
