package com.example.MyBookShopApp.dto.book;

import lombok.Data;

@Data
public class ChangeStatusPayload {
    private Integer[] bookIds;
    private String status;
}
