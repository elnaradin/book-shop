package com.example.MyBookShopApp.model.book.links;

import com.example.MyBookShopApp.model.book.BookEntity;
import com.example.MyBookShopApp.model.book.RatingEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "book2rating")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book2RatingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private BookEntity book;
    @ManyToOne
    private RatingEntity rating;
}
