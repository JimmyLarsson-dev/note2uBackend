package com.example.note2ubackendnosecurity.user;

import com.example.note2ubackendnosecurity.notes.NoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository <UserEntity, UUID>{
    boolean existsByIdAndNotesContains(UUID userId, NoteEntity note);


    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUsername(String username);

}
