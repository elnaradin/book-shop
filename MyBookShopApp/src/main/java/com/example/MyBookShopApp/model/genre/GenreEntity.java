package com.example.MyBookShopApp.model.genre;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@ApiModel("entity representing a book genre")
@Entity
@Table(name = "genres")
@Getter
@Setter
public class GenreEntity {
    @ApiModelProperty("auto generated id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @ApiModelProperty("id of the parent genre")
    private Integer parentId;

    @ApiModelProperty("mnemonic identifier")
    private String slug;

    @ApiModelProperty("name of the genre")
    private String name;

}
