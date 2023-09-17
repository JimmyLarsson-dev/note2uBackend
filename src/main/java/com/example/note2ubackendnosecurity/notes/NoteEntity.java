package com.example.note2ubackendnosecurity.notes;

import com.example.note2ubackendnosecurity.user.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
//@RequiredArgsConstructor
//@AllArgsConstructor
@Table(name = "notes")
public class NoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "note_id")
    UUID id;
    String title;
    String content;
//    @Column(name = "user_id")
    @ManyToMany
    @Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "userId")
    private List<UserEntity> users;

    public NoteEntity(String title, String content, UserEntity user) {
        this.title = title;
        this.content = content;
        this.users = List.of(user);
    }
}