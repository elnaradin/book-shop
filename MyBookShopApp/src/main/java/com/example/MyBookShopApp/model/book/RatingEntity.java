package com.example.MyBookShopApp.model.book;

import com.example.MyBookShopApp.model.book.links.Book2RatingEntity;
import com.example.MyBookShopApp.model.user.UserEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "ratings")
@Getter
@Setter
public class RatingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer value;

    @ManyToOne
    private UserEntity user;

    @OneToMany(orphanRemoval = true, mappedBy = "rating", fetch = FetchType.LAZY)
    private List<Book2RatingEntity> book2RatingEntityList = new ArrayList<>();
}
