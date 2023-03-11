package com.example.MyBookShopApp.model.book.authors;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@ApiModel("entity representing an author")
@Entity
@Table(name = "authors")
@Getter
@Setter
public class AuthorEntity {
    @ApiModelProperty("auto generated id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    @ApiModelProperty("author's photo")
    private String photo;
    @ApiModelProperty("mnemonic id of an author")
    @Column(nullable = false)
    private String slug;
    @ApiModelProperty("author's name")
    @Column(nullable = false)
    private String name;
    @ApiModelProperty("author's description")
    @Column(columnDefinition = "TEXT")
    private String description;

//    @ApiModelProperty(hidden = true)
//    @JsonManagedReference
//    @OneToMany(mappedBy = "author",
//            cascade = CascadeType.ALL,
//            fetch = FetchType.LAZY)
//    private Set<Book2AuthorEntity> bookLinks = new HashSet<>();

}
