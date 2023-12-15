package com.example.note2ubackendnosecurity.notes;

import com.example.note2ubackendnosecurity.user.UserEntity;
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
@Table(name = "notes")
public class NoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "note_id")
    UUID id;

    private boolean isChecklist;
    private String title;
    private String textContent;
    private String content;

    @ManyToMany
    @Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "userId")
    private List<UserEntity> users;

    public NoteEntity(String title, String textContent, UserEntity user) {
        this.title = title;
        this.textContent = textContent;
        this.users = List.of(user);
    }
}
