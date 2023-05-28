package com.example.MyBookShopApp.model.user;

import com.example.MyBookShopApp.model.book.review.BookReviewEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApiModel("entity representing a user")
@Entity
@Table(name = "users")
@Getter
@Setter
public class UserEntity {
    @ApiModelProperty("auto generated id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ApiModelProperty("full name of the user")
    private String name;
    private String email; // not defined in TS
    private String phone; //not defined in TS
    private String password; //not defined in TS
    @ApiModelProperty("random hash code for identification")
    private String hash;
    @ApiModelProperty("date and time of registration")
    private LocalDateTime regTime;
    @ApiModelProperty("bank balance of the user")
    private int balance;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<BookReviewEntity> reviews = new ArrayList<>();
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<User2RoleEntity> user2role = new ArrayList<>();

}
