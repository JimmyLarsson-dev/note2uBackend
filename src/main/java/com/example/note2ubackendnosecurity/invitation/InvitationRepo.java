package com.example.note2ubackendnosecurity.invitation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InvitationRepo extends JpaRepository<Invitation, UUID> {

    List<Invitation> findByRecipientId(UUID recipientId);
}
