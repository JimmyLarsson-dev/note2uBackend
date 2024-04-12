package com.example.note2ubackendnosecurity.utilities;

import com.example.note2ubackendnosecurity.acceptNoteQuery.AcceptNoteQuery;
import com.example.note2ubackendnosecurity.acceptNoteQuery.AcceptNoteQueryRepo;
import com.example.note2ubackendnosecurity.checklist.ChecklistRepo;
import com.example.note2ubackendnosecurity.exceptions.UserMissingException;
import com.example.note2ubackendnosecurity.notes.DTOs.InvitationRequest;
import com.example.note2ubackendnosecurity.notes.NoteRepo;
import com.example.note2ubackendnosecurity.user.UserEntity;
import com.example.note2ubackendnosecurity.user.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class InvitationsHandler {

    private final AcceptNoteQueryRepo acceptNoteQueryRepo;
    private final VerifyUserInput verifyUserInput;
    private final UserRepo userRepo;
    private final NoteRepo noteRepo;
    private final ChecklistRepo checklistRepo;

    public InvitationsHandler(
            AcceptNoteQueryRepo acceptNoteQueryRepo,
            VerifyUserInput verifyUserInput,
            UserRepo userRepo,
            NoteRepo noteRepo,
            ChecklistRepo checklistRepo) {
        this.acceptNoteQueryRepo = acceptNoteQueryRepo;
        this.verifyUserInput = verifyUserInput;
        this.userRepo = userRepo;
        this.noteRepo = noteRepo;
        this.checklistRepo = checklistRepo;
    }

    private void createAcceptNoteQuery(InvitationRequest request, UserEntity user) {
        acceptNoteQueryRepo.save(new AcceptNoteQuery(
                UUID.randomUUID(),
                noteOrChecklist(UUID.fromString(request.getItemId())),
                UUID.fromString(request.getItemId()),
                UUID.fromString(request.getInviterId()),
                user.getId(),
                request.getTitle()));
    }

    public String inviteByEmail(InvitationRequest request) throws UserMissingException {
        verifyUserInput.verifyEmailExists(request.getRecipientEmail());
        verifyUserInput.verifyNotInvitingSelfByEmail(request.getInviterId(), request.getRecipientEmail());
        UserEntity recipientUser = userRepo.findByEmail(request.getRecipientEmail()).get();
        if (verifyUserInput.verifyIfSenderIsBlocked(
                request.getInviterId(),
                recipientUser.getId())) {
            return "blocked";
        }
        createAcceptNoteQuery(request, recipientUser);
        return "invitation sent";
    }

    private boolean noteOrChecklist(UUID itemId) {
        if (noteRepo.existsById(itemId)) {
            return false;
        } else if (checklistRepo.existsById(itemId)) {
            return true;
        } else {
            throw new EntityNotFoundException("No such checklist or note found");
        }
    }

    public String inviteByUsername(InvitationRequest request) throws UserMissingException {
        verifyUserInput.verifyUsernameExists(request.getRecipientUsername());
        verifyUserInput.verifyNotInvitingSelfByUsername(request.getInviterId(), request.getRecipientUsername());
        UserEntity recipientUser = userRepo.findByUsername(request.getRecipientUsername()).get();
        if (verifyUserInput.verifyIfSenderIsBlocked(
                request.getInviterId(),
                recipientUser.getId())) {
            return "blocked";
        }
        createAcceptNoteQuery(request, recipientUser);
        return "invitation sent";
    }
}
