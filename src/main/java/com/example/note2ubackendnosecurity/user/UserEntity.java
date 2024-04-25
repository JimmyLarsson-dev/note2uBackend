package com.example.note2ubackendnosecurity.user;

import com.example.note2ubackendnosecurity.checklist.ChecklistEntity;
import com.example.note2ubackendnosecurity.notes.NoteEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private UUID id;

    private String email;
    private String username;
    private String password;

    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    @Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.PERSIST})
    private List<NoteEntity> notes;

    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    @Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.PERSIST})
    private List<ChecklistEntity> checkLists;

    private String language;

    @OneToMany
    private List<UserEntity> blockedUsers;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
