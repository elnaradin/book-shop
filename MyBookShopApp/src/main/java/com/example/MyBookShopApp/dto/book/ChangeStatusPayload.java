package com.example.MyBookShopApp.dto.book;

import com.example.MyBookShopApp.model.enums.StatusType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeStatusPayload {
    private List<String> bookIds;
    private StatusType status;
}
