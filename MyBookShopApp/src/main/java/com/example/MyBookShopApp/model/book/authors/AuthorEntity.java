package com.example.MyBookShopApp.model.book.authors;

import com.example.MyBookShopApp.model.book.links.Book2AuthorEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "authors")
@Getter
@Setter
public class AuthorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String photo;
    @Column(nullable = false)
    private String slug;
    @Column(nullable = false)
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;

    @JsonManagedReference
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private Set<Book2AuthorEntity> books = new HashSet<>();

}
