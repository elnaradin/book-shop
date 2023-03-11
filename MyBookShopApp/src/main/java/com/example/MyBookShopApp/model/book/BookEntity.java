package com.example.MyBookShopApp.model.book;

import com.fasterxml.jackson.annotation.JsonGetter;
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
    @Column(columnDefinition = "SMALLINT NOT NULL")
    @ApiModelProperty("if isBestseller = 1, the book is considered to be a bestseller, if 0, the book is not a bestseller")
    private Integer isBestseller;
    @Column(nullable = false)
    @ApiModelProperty("mnemonic identity sequence")
    private String slug;
    @Column(nullable = false)
    @ApiModelProperty("book title")
    private String title;
    @ApiModelProperty("image url")
    private String image;
    @Column(columnDefinition = "TEXT")
    @ApiModelProperty("the description of the book")
    private String description;
    @Column(nullable = false)
    @ApiModelProperty("book price without discount")
    private Integer price;
    @Column(columnDefinition = "SMALLINT NOT NULL DEFAULT 0")
    @ApiModelProperty("discount value for book")
    private Integer discount;

//    @ManyToMany(mappedBy = "books")
//    @JsonManagedReference
//    private Set<RatingEntity> ratings = new HashSet<>();
//
//    @ApiModelProperty("book's status: 'KEPT', 'CART', 'PAID' OR 'ARCHIVED'")
//    @JsonBackReference
//    @Enumerated
//    private StatusType status;


//    @JsonManagedReference
//    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
//    private Set<Book2AuthorEntity> authorLinks = new HashSet<>();
//
//    @JsonManagedReference
//    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private Set<Book2UserEntity> userLinks = new HashSet<>();
//
//    @JsonManagedReference
//    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private Set<Book2TagEntity> tagLinks = new HashSet<>();
//    @JsonManagedReference
//    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private Set<Book2GenreEntity> genreLinks = new HashSet<>();

//    @JsonManagedReference
//    @OneToMany(mappedBy = "book")
//    private Set<BookFileEntity> files = new HashSet<>();

    @JsonGetter("discountPrice")
    public Integer discountPrice() {
        return Math.toIntExact(Math.round(price * (1.0 - (double) discount / 100)));
    }

//    @JsonGetter("authors")
//    public String getAuthorNames(){
//        StringBuilder builder = new StringBuilder();
//        for(Book2AuthorEntity link : authorLinks){
//            builder.append(builder.length() == 0 ? " ": ", ").append(link.getAuthor().getName());
//        }
//        if(builder.toString().isBlank()){
//            builder.append("no author in db");
//        }
//        return builder.toString().trim();
//
//    }

//    @JsonGetter("rating")
//    public Integer getRating(){
//        Integer sum = ratings.stream()
//                .map(RatingEntity::getValue)
//                .reduce(0, Integer::sum);
//        return Math.round((float)sum / ratings.size());
//    }
//
//    public Long getRatingAmountByStars(int starAmount){
//        return ratings.stream().filter(rating -> rating.getValue() == starAmount).count();
//    }
}
