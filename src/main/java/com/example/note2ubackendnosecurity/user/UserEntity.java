package com.example.note2ubackendnosecurity.user;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
//@RequiredArgsConstructor
@AllArgsConstructor
//@Table(name = "users")
@Builder
public class UserEntity {

    @Id
//    @GeneratedValue
    @Column(name = "id")
    private UUID id;
    private String email;
    private String username;
    private String password;

//    public UserEntity() {
//    }
}
