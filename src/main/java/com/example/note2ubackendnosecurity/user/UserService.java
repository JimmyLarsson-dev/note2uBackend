package com.example.note2ubackendnosecurity.user;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    UserRepo repo;


    public String register(String username, String password, String email) {

        UserEntity user = new UserEntity(
                UUID.randomUUID(),
                username,
                password,
                email);

        repo.save(user);

        return "User created";
    }
}
