package com.example.mybookshopapp.dto.admin;

import lombok.Data;

@Data
public class DeleteItemDto {
    private ItemType item;
    private String itemId;
}
