package com.example.MyBookShopApp.dto.book;

import com.example.MyBookShopApp.model.enums.StatusType;
import lombok.Data;

import java.util.List;

@Data
public class ChangeStatusPayload {
    private List<String> bookIds;
    private StatusType status;
}
