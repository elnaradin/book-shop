package com.example.MyBookShopApp.model.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "user2role")
public class User2RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    @ManyToOne(cascade = CascadeType.MERGE)
    private UserEntity user;

    @ManyToOne(cascade = CascadeType.MERGE)
    private UserRoleEntity role;

}
