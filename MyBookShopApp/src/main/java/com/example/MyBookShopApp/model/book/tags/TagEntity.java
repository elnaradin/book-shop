package com.example.MyBookShopApp.model.book.tags;

import com.example.MyBookShopApp.model.book.links.Book2TagEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
@Getter
@Setter
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String name;
    @JsonManagedReference
    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
    private Set<Book2TagEntity> books = new HashSet<>();
}
