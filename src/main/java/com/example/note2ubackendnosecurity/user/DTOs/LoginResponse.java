package com.example.note2ubackendnosecurity.user.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {

    private String id;
    private String username;
    private String email;

}
