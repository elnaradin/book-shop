package com.example.mybookshopapp.model.book.review;

import com.example.mybookshopapp.model.book.BookEntity;
import com.example.mybookshopapp.model.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_review")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private BookEntity book;

    @ManyToOne
    private UserEntity user;
    private LocalDateTime time;
    private String text;
    private String hash;


}
