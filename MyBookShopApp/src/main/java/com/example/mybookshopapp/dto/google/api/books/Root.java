package com.example.mybookshopapp.dto.google.api.books;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Root {
    private String kind;
    private long totalItems;
    private ArrayList<Item> items;


}
