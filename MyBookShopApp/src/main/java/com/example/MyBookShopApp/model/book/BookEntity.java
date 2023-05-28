package com.example.MyBookShopApp.model.book;

import com.example.MyBookShopApp.model.book.links.Book2AuthorEntity;
import com.example.MyBookShopApp.model.book.links.Book2RatingEntity;
import com.example.MyBookShopApp.model.book.links.Book2TagEntity;
import com.example.MyBookShopApp.model.book.review.BookReviewEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private List<Book2AuthorEntity> linksToAuthors = new ArrayList<>();
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private List<Book2RatingEntity> linksToRatings = new ArrayList<>();
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private List<Book2TagEntity> linksToTags = new ArrayList<>();
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private List<BookReviewEntity> reviews = new ArrayList<>();
}
