package com.example.mybookshopapp.model.book;

import com.example.mybookshopapp.model.book.tags.TagEntity;
import com.example.mybookshopapp.model.genre.GenreEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@ApiModel("entity representing a book")
@Entity
@Table(name = "books")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookEntity {
    @ApiModelProperty("auto generated id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "book2tag",
            joinColumns = @JoinColumn(
                    name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "tag_id", referencedColumnName = "id"))
    private List<TagEntity> tags = new ArrayList<>();
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "book2genre",
            joinColumns = @JoinColumn(
                    name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "genre_id", referencedColumnName = "id"))
    private List<GenreEntity> genres = new ArrayList<>();



}
