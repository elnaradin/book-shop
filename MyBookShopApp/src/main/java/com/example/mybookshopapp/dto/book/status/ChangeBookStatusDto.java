package com.example.mybookshopapp.dto.book.status;

import com.example.mybookshopapp.model.enums.StatusType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeBookStatusDto {
    private List<String> bookIds;
    private StatusType status;
}
