package com.example.note2ubackendnosecurity.user;

import com.example.note2ubackendnosecurity.notes.NoteEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private UUID id;
    private String email;
    private String username;
    private String password;
    @ManyToMany
    @JoinTable(name = "user_note", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "note_id"))
    @Column(name = "note_id")
    private List<NoteEntity> notes;

    public UserEntity(String email, String username, String password, List<NoteEntity> notes) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.notes = notes;
    }
}
