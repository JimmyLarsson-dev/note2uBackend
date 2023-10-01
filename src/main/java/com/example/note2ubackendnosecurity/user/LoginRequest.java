package com.example.note2ubackendnosecurity.user;

import lombok.Data;

@Data
public class LoginRequest {

    private String username;
    private String password;

}
