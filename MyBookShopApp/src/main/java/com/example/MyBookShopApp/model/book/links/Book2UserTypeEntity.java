package com.example.MyBookShopApp.model.book.links;

import com.example.MyBookShopApp.model.enums.StatusType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "book2user_type")
@Getter
@Setter
public class Book2UserTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Enumerated(value = EnumType.STRING)
    private StatusType code;

    private String name;

}
