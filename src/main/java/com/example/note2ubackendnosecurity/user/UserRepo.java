package com.example.note2ubackendnosecurity.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepo extends JpaRepository <UserEntity, UUID>{

}
