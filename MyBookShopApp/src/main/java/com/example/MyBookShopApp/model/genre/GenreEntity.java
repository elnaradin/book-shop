package com.example.MyBookShopApp.model.genre;

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
    @Column(columnDefinition = "INT")
    private Integer parentId;

    @ApiModelProperty("mnemonic identifier")
    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String slug;

    @ApiModelProperty("name of the genre")
    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

}
