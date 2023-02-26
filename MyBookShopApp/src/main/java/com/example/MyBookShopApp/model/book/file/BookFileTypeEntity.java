package com.example.MyBookShopApp.model.book.file;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "book_file_type")
@Getter
@Setter
public class BookFileTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

}
