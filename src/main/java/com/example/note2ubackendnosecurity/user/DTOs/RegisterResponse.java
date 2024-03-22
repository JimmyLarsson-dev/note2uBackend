package com.example.note2ubackendnosecurity.user.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class RegisterResponse {

    private String id;
    private String username;
    private String email;

}
