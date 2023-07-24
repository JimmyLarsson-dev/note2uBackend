package com.example.note2ubackendnosecurity.user;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo repo;

    public String register(String username, String password, String email) {

        if(repo.findAll()
                .stream()
                .anyMatch(x -> x.getEmail().equals(email))){
            return "email already exists!";
        }

        if(repo.findAll()
                .stream()
                .anyMatch(x -> x.getUsername().equals(username) )) {
            return "user name already exists!";
        }

        UserEntity user = new UserEntity(
                UUID.randomUUID(),
                email,
                username,
                password
                );

        repo.save(user);

        return "User created";
    }
}
