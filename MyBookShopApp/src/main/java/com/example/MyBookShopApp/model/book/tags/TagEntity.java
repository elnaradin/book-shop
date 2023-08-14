package com.example.MyBookShopApp.model.book.tags;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@ApiModel("entity representing book's tag")
@Entity
@Table(name = "tags")
@Getter
@Setter
public class TagEntity {
    @ApiModelProperty("auto generated id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String slug;
    @ApiModelProperty("name of a tag")
    private String name;

}
