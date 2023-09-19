package com.example.mybookshopapp.model.book.file;

import com.example.mybookshopapp.model.book.BookEntity;
import com.example.mybookshopapp.model.enums.BookFileType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@ApiModel("entity representing a book file")
@Entity
@Table(name = "book_file")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookFileEntity {
    @ApiModelProperty("auto generated id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ApiModelProperty("random hash code for identification")
    private String hash;
    @ApiModelProperty("file type (PDF, EPUB or FB2)")
    @ManyToOne
    private BookFileTypeEntity type;
    @ApiModelProperty("file path")
    private String path;

    @ApiModelProperty("the book")
    @ManyToOne
    private BookEntity book;

    @ApiModelProperty(hidden = true)
    @Transient
    private String bookFileExtensionString;

    public String getBookFileExtensionString() {
        return BookFileType.getExtensionStringByTypeId(type.getId());
    }
}

