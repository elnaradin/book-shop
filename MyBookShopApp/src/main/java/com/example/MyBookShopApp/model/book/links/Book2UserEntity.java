package com.example.MyBookShopApp.model.book.links;

import com.example.MyBookShopApp.model.book.BookEntity;
import com.example.MyBookShopApp.model.user.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "book2user")
@Getter
@Setter
public class Book2UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime time;

    @ManyToOne
    private Book2UserTypeEntity type;

    @ManyToOne
    private BookEntity book;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity user;

}
