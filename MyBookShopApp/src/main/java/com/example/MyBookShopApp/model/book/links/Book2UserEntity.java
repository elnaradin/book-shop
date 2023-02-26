package com.example.MyBookShopApp.model.book.links;

import com.example.MyBookShopApp.model.book.BookEntity;
import com.example.MyBookShopApp.model.user.UserEntity;
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
import java.time.LocalDateTime;

@Entity
@Table(name = "book2user")
@Getter
@Setter
public class Book2UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    private LocalDateTime time;

    @JoinColumn(columnDefinition = "INT NOT NULL")
    @ManyToOne
    private Book2UserTypeEntity type;

    @ManyToOne
    @JoinColumn(columnDefinition = "INT NOT NULL")
    @JsonBackReference
    private BookEntity book;

    @ManyToOne
    @JoinColumn(columnDefinition = "INT NOT NULL")
    @JsonBackReference
    private UserEntity user;

}
