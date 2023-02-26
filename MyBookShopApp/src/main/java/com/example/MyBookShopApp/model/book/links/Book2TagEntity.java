package com.example.MyBookShopApp.model.book.links;

import com.example.MyBookShopApp.model.book.BookEntity;
import com.example.MyBookShopApp.model.book.tags.TagEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "book2tag")
@Getter
@Setter
public class Book2TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    @ManyToOne
    @JsonBackReference
    private BookEntity book;
    @ManyToOne
    @JsonBackReference
    private TagEntity tag;
}
