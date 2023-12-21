package com.example.note2ubackendnosecurity.notes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NoteRepo extends JpaRepository<NoteEntity, UUID> {


//    Optional<NoteEntity> findByUserId(UUID uuid);
}
