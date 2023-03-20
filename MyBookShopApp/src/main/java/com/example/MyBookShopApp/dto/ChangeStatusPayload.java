package com.example.MyBookShopApp.dto;

import lombok.Data;

@Data
public class ChangeStatusPayload {
    private int[] bookIds;
    private String status;
}
