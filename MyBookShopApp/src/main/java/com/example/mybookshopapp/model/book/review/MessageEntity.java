package com.example.mybookshopapp.model.book.review;

import com.example.mybookshopapp.model.user.UserEntity;
import lombok.Getter;
import lombok.Setter;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDateTime time;

    @JoinColumn(columnDefinition = "INT")
    @ManyToOne
    private UserEntity user;

    private String email;
    private String name;
    private String subject;
    private String text;

}
