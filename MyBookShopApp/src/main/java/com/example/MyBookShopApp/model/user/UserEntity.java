package com.example.MyBookShopApp.model.user;

import com.example.MyBookShopApp.model.book.links.Book2UserEntity;
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
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String hash;

    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    private LocalDateTime regTime;

    @Column(columnDefinition = "INT NOT NULL")
    private int balance;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Book2UserEntity> books = new HashSet<>();
}
