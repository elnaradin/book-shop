package com.example.mybookshopapp.dto.book.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PubDatesDto {

    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate from;

    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate to;
}
