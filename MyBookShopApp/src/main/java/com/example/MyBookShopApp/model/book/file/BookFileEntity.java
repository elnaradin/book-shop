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
@Table(name = "book_file")
@Getter
@Setter
public class BookFileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @Column(nullable = false)
    private String hash;
    @Column(nullable = false)
    private Integer typeId;
    @Column(nullable = false)
    private String path;
}

