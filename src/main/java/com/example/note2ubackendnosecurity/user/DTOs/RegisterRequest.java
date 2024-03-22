package com.example.note2ubackendnosecurity.user.DTOs;

import lombok.Data;

@Data
public class RegisterRequest {

    private String username;
    private String password;
    private String email;
    private String language;

}
