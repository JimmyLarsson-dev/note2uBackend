package com.example.note2ubackendnosecurity.notes;

import com.example.note2ubackendnosecurity.user.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "notes")
public class NoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "note_id")
    UUID id;
    String title;
    String content;
    @Column(name = "user_id")
    @ManyToMany(mappedBy = "notes", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("notes")
    private List<UserEntity> users;

    public NoteEntity(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
