package com.example.MyBookShopApp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HeaderValues {
    private Boolean isAuthorized;
    private Integer cart;
    private Integer postponed;
    private Integer my;
    private Integer balance;
    private String username;


}
