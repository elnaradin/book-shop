package com.example.MyBookShopApp.model.book;

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
import java.time.LocalDate;


@ApiModel("entity representing a book")
@Entity
@Table(name = "books")
@Getter
@Setter
public class BookEntity {
    @ApiModelProperty("auto generated id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    @Column(columnDefinition = "DATE")
    @ApiModelProperty("book publication date")
    private LocalDate pubDate;

    @ApiModelProperty("if isBestseller = 1, the book is considered to be a bestseller, if 0, the book is not a bestseller")
    private Integer isBestseller;

    @ApiModelProperty("mnemonic identity sequence")
    private String slug;

    @ApiModelProperty("book title")
    private String title;
    @ApiModelProperty("image url")
    private String image;

    @ApiModelProperty("the description of the book")
    private String description;

    @ApiModelProperty("book price without discount")
    private Integer price;

    @ApiModelProperty("discount value for book")
    private Integer discount;

}
