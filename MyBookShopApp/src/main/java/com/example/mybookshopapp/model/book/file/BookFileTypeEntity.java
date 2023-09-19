package com.example.mybookshopapp.model.book.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@ApiModel("entity representing a book file type")
@Entity
@Table(name = "book_file_type")
@Getter
@Setter
public class BookFileTypeEntity {
    @ApiModelProperty("auto generated id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ApiModelProperty("file type name")
    private String name;

    @ApiModelProperty("file type description")
    private String description;

}
