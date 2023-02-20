package com.example.MyBookShopApp.model.book;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "books")
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Date pubDate;
    @Column(columnDefinition = "SMALLINT NOT NULL")
    private Integer isBestSeller;
    @Column(nullable = false)
    private String slug;
    @Column(nullable = false)
    private String title;
    private String image;

    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(nullable = false)
    private Integer price;
    @Column(columnDefinition = "SMALLINT NOT NULL DEFAULT 0")
    private Integer discount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public Integer getIsBestSeller() {
        return isBestSeller;
    }

    public void setIsBestSeller(Integer isBestSeller) {
        this.isBestSeller = isBestSeller;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

//    @ManyToOne
//    @JoinColumn(name = "author_id", referencedColumnName = "id")
//    private Author author;
//    private String title;
//
//    private String priceOld;
//    private String price;


}
