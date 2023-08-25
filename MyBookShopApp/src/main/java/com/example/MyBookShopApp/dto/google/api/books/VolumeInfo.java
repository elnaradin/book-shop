package com.example.MyBookShopApp.dto.google.api.books;

import lombok.Data;

import java.util.ArrayList;

@Data
public class VolumeInfo {
    private String title;
    private ArrayList<String> authors;
    private String publisher;
    private String publishedDate;
    private String description;
    private ReadingModes readingModes;
    private int pageCount;
    private String printType;
    private ArrayList<String> categories;
    private String maturityRating;
    private boolean allowAnonLogging;
    private String contentVersion;
    private PanelizationSummary panelizationSummary;
    private ImageLinks imageLinks;
    private String language;
    private String previewLink;
    private String infoLink;
    private String canonicalVolumeLink;
    private ArrayList<IndustryIdentifier> industryIdentifiers;


}
