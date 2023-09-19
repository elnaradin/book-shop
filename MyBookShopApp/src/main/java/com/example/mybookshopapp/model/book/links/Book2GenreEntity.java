package com.example.mybookshopapp.model.book.links;

import com.example.mybookshopapp.model.book.BookEntity;
import com.example.mybookshopapp.model.genre.GenreEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "book2genre")
@Getter
@Setter
public class Book2GenreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    private BookEntity book;

    @ManyToOne(cascade = CascadeType.ALL)
    private GenreEntity genre;
}
