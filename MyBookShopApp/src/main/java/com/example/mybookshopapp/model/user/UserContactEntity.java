package com.example.mybookshopapp.model.user;

import com.example.mybookshopapp.model.enums.ContactType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_contact")
@Getter
@Setter
@NoArgsConstructor
public class UserContactEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private UserEntity user;
    private ContactType type;
    private int approved;
    private String code;
    private int codeTrails;
    private LocalDateTime codeTime;
    private String contact;

    public UserContactEntity(String code, Integer expireIn, UserEntity user, String contact, ContactType type) {
        this.code = code;
        this.codeTime = LocalDateTime.now().plusMinutes(expireIn);
        codeTrails = 0;
        approved = 0;
        this.contact = contact;
        this.user = user;
        this.type=type;
    }
}
