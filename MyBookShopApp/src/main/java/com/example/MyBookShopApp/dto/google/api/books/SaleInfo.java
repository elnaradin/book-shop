package com.example.MyBookShopApp.dto.google.api.books;

import lombok.Data;

import java.util.ArrayList;

@Data
public class SaleInfo {

    private String country;
    private boolean isEbook;
    private ListPrice listPrice;
    private RetailPrice retailPrice;
    private String buyLink;
    private ArrayList<Offer> offers;
    String saleability;


}
