package com.example.MyBookShopApp.model.book.review;

import com.example.MyBookShopApp.model.book.BookEntity;
import com.example.MyBookShopApp.model.user.UserEntity;
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
import java.time.LocalDateTime;

@Entity
@Table(name = "book_review")
@Getter
@Setter
public class BookReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(columnDefinition = "INT NOT NULL")
    @ManyToOne
    private BookEntity book;

    @ManyToOne
    @JoinColumn(columnDefinition = "INT NOT NULL")
    private UserEntity user;

    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    private LocalDateTime time;

    @Column(columnDefinition = "TEXT NOT NULL")
    private String text;


}
