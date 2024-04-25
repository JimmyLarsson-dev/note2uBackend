package com.example.note2ubackendnosecurity.acceptNoteQuery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AcceptNoteQueryRepo extends JpaRepository<AcceptNoteQuery, UUID> {

    Optional<AcceptNoteQuery> findByRecipientId(UUID recipientId);
}
