package com.example.MyBookShopApp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ResultDto {
    private boolean result;
    private String error;
}
