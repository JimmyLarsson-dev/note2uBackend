package com.example.note2ubackendnosecurity.user.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class RegisterResponse {

    private String id;
    private String token;
    private String username;
    private String email;

}
