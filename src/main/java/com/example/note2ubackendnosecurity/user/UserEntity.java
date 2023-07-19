package com.example.note2ubackendnosecurity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class UserEntity {

    @Id
    @Column(name = "id")
    public UUID id;
    public String email;
    public String username;
    public String password;

}
