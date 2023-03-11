package com.example.MyBookShopApp.model.book.file;

import com.example.MyBookShopApp.model.book.BookEntity;
import com.example.MyBookShopApp.model.enums.BookFileType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@ApiModel("entity representing a book file")
@Entity
@Table(name = "book_file")
@Getter
@Setter
public class BookFileEntity {
    @ApiModelProperty("auto generated id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @ApiModelProperty("random hash code for identification")
    @Column(nullable = false)
    private String hash;
    @ApiModelProperty("file type (PDF, EPUB or FB2)")
    @JoinColumn(nullable = false)
    @ManyToOne
    private BookFileTypeEntity type;
    @ApiModelProperty("file path")
    @Column(nullable = false)
    private String path;

    @ApiModelProperty("the book")
    @JsonBackReference
    @ManyToOne
    private BookEntity book;

    @ApiModelProperty(hidden = true)
    private String bookFileExtensionString;


    public String getBookFileExtensionString() {
        return BookFileType.getExtensionStringByTypeId(type.getId());
    }
}

