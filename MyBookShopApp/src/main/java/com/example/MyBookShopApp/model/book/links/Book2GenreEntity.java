package com.example.MyBookShopApp.model.book.links;

import com.example.MyBookShopApp.model.book.BookEntity;
import com.example.MyBookShopApp.model.genre.GenreEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "book2genre")
@Getter
@Setter
public class Book2GenreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @JoinColumn(columnDefinition = "INT NOT NULL")
    @ManyToOne(cascade = CascadeType.ALL)
    private BookEntity book;

    @JoinColumn(columnDefinition = "INT NOT NULL")
    @ManyToOne(cascade = CascadeType.ALL)
    private GenreEntity genre;
}
