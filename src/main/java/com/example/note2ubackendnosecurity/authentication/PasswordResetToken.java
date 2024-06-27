package com.example.note2ubackendnosecurity.authentication;

import com.example.note2ubackendnosecurity.user.UserEntity;
import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
public class PasswordResetToken {

    private static final int EXPIRATION = 60*24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String token;

    @OneToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private UserEntity user;

    private Date expiryDate;
}
