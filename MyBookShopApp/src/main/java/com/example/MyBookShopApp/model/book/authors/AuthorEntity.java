package com.example.MyBookShopApp.model.book.authors;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@ApiModel("author entity")
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
    private String slug;
    @ApiModelProperty("author's name")
    private String name;
    @ApiModelProperty("author's description")
    private String description;

}
