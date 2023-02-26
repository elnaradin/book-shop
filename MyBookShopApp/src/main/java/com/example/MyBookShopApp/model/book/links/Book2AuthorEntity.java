package com.example.MyBookShopApp.model.book.links;

import com.example.MyBookShopApp.model.book.authors.AuthorEntity;
import com.example.MyBookShopApp.model.book.BookEntity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "book2author")
@Getter
@Setter
public class Book2AuthorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @JoinColumn(columnDefinition = "INT NOT NULL")
    @ManyToOne
    @JsonBackReference
    private BookEntity book;

    @JoinColumn(columnDefinition = "INT NOT NULL")
    @ManyToOne
    @JsonBackReference
    private AuthorEntity author;

    @Column(columnDefinition = "INT NOT NULL  DEFAULT 0")
    private int sortIndex;

}
