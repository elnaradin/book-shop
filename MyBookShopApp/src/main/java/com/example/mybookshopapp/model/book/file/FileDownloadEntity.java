package com.example.mybookshopapp.model.book.file;

import com.example.mybookshopapp.model.book.BookEntity;
import com.example.mybookshopapp.model.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "file_download")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileDownloadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private UserEntity user;

    @ManyToOne
    private BookEntity book;

    private int count;

    public FileDownloadEntity(UserEntity user, BookEntity book) {
        this.user = user;
        this.book = book;
        count = 0;
    }
}
