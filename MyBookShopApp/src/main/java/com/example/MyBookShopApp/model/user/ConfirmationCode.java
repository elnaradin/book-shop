package com.example.MyBookShopApp.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "confirmation_codes")
@Getter
@Setter
@NoArgsConstructor
public class ConfirmationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String value;
    private LocalDateTime expiryTime;

    public ConfirmationCode(String value, Integer expireIn) {
        this.value = value;
        this.expiryTime = LocalDateTime.now().plusSeconds(expireIn);
    }
}
