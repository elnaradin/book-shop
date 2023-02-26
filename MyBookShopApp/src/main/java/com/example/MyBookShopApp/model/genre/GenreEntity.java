package com.example.MyBookShopApp.model.genre;

import com.example.MyBookShopApp.model.book.links.Book2GenreEntity;
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
@Table(name = "genres")
@Getter
@Setter
public class GenreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(columnDefinition = "INT")
    private Integer parentId;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String slug;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Book2GenreEntity> books = new HashSet<>();
}
