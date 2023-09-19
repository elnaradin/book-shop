package com.example.mybookshopapp.model.payments;

import com.example.mybookshopapp.model.book.BookEntity;
import com.example.mybookshopapp.model.user.UserEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "balance_transaction")
@Getter
@Setter
public class BalanceTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private UserEntity user;

    private LocalDateTime time;

    private Integer value;
    @ManyToOne
    private BookEntity book;

    private String description;


}
