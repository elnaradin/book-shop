package com.example.MyBookShopApp.model.book.review;

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
@Table(name = "message")
@Getter
@Setter
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    private LocalDateTime time;

    @JoinColumn(columnDefinition = "INT")
    @ManyToOne
    private UserEntity user;

    @Column(columnDefinition = "VARCHAR(255)")
    private String email;

    @Column(columnDefinition = "VARCHAR(255)")
    private String name;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String subject;

    @Column(columnDefinition = "TEXT NOT NULL")
    private String text;

}
