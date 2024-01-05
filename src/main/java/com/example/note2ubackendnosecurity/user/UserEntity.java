package com.example.note2ubackendnosecurity.user;

import com.example.note2ubackendnosecurity.checklist.ChecklistEntity;
import com.example.note2ubackendnosecurity.notes.NoteEntity;
import com.example.note2ubackendnosecurity.other.Languages;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
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
    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    @Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.PERSIST})
//    @JoinColumn(name = "note_id")
    private List<NoteEntity> notes;

    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    @Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.PERSIST})
    private List<ChecklistEntity> checkLists;

    private Languages language;

//    @ManyToMany(mappedBy = "user_id")
//    @Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.PERSIST})
    @OneToMany
    private List<UserEntity> blockedUsers;

    public UserEntity(String email, String username, String password, List<NoteEntity> notes, List<UserEntity> blockedUsers, Languages language) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.notes = notes;
        this.blockedUsers = blockedUsers;
        this.language = language;
    }
}
