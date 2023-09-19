package com.example.mybookshopapp.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoPage {
    private long count;
    private boolean hasNext;
    private List<UserDto> users;
}
