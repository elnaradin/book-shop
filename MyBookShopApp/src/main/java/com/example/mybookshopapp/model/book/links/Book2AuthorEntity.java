package com.example.mybookshopapp.model.book.links;

import com.example.mybookshopapp.model.book.BookEntity;
import com.example.mybookshopapp.model.book.authors.AuthorEntity;
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

@Entity
@Table(name = "book2author")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book2AuthorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    private BookEntity book;
    @ManyToOne
    private AuthorEntity author;
    private int sortIndex;


}
