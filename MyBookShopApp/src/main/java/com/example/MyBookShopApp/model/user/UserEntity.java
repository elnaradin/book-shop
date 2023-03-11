package com.example.MyBookShopApp.model.user;

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
import java.time.LocalDateTime;

@ApiModel("entity representing a user")
@Entity
@Table(name = "users")
@Getter
@Setter
public class UserEntity {
    @ApiModelProperty("auto generated id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ApiModelProperty("random hash code for identification")
    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String hash;

    @ApiModelProperty("date and time of registration")
    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    private LocalDateTime regTime;

    @ApiModelProperty("bank balance of a user")
    @Column(columnDefinition = "INT NOT NULL")
    private int balance;

    @ApiModelProperty("full name of a user")
    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

}
