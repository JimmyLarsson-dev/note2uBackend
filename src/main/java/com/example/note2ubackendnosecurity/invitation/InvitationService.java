package com.example.note2ubackendnosecurity.invitation;

import com.example.note2ubackendnosecurity.invitation.DTOs.InvitationRequest;
import com.example.note2ubackendnosecurity.invitation.DTOs.DeclineNoteRequest;
import com.example.note2ubackendnosecurity.invitation.DTOs.InvitationResponse;
import com.example.note2ubackendnosecurity.checklist.ChecklistEntity;
import com.example.note2ubackendnosecurity.checklist.ChecklistRepo;
import com.example.note2ubackendnosecurity.exceptions.InvalidInputException;
import com.example.note2ubackendnosecurity.exceptions.NoteMissingException;
import com.example.note2ubackendnosecurity.exceptions.UserMissingException;
import com.example.note2ubackendnosecurity.notes.DTOs.GetNoteResponse;
import com.example.note2ubackendnosecurity.notes.NoteEntity;
import com.example.note2ubackendnosecurity.notes.NoteRepo;
import com.example.note2ubackendnosecurity.user.UserEntity;
import com.example.note2ubackendnosecurity.user.UserRepo;
import com.example.note2ubackendnosecurity.utilities.VerifyUserInput;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InvitationService {

    private final VerifyUserInput verifyUserInput;
    private final InvitationRepo invitationRepo;
    private final NoteRepo noteRepo;
    private final ChecklistRepo checklistRepo;
    private final UserRepo userRepo;

    public InvitationService(
            VerifyUserInput verifyUserInput,
            InvitationRepo invitationRepo,
            NoteRepo noteRepo,
            ChecklistRepo checklistRepo,
            UserRepo userRepo) {
        this.verifyUserInput = verifyUserInput;
        this.invitationRepo = invitationRepo;
        this.noteRepo = noteRepo;
        this.checklistRepo = checklistRepo;
        this.userRepo = userRepo;
    }

    //TODO hantera detta snyggare! Nu returneras bara den första i en lista, borde returnera alla inbjudningar!!!!!!!
    public InvitationResponse checkReceivedNotes(String userId) throws NoteMissingException, UserMissingException {
        verifyUserInput.verifyIfUserExists(userId);
        List<Invitation> optionalInvitation = invitationRepo.findByRecipientId(UUID.fromString(userId));
        if(optionalInvitation.isEmpty()) {
            throw new NoteMissingException("No new notes");
        }
        String senderUsername = userRepo.findById(optionalInvitation.get(0)
                .getSenderId()).get().getUsername();
        String noteTitle = noteRepo.findById(optionalInvitation.get(0)
                .getItemId()).get().getTitle();
        return new InvitationResponse(
                senderUsername,
                noteTitle,
                optionalInvitation.get(0).getRequestId().toString(),
                optionalInvitation.get(0).getItemId().toString());
    }

    public GetNoteResponse acceptNote(InvitationRequest invitationRequest) {
        verifyUserInput.verifyIfAcceptNoteQueryExists(invitationRequest.getRequestId());
        Invitation invitation = invitationRepo.findById(UUID.fromString(invitationRequest.getRequestId())).get();
        if(!invitation.getRecipientId().toString().equals(invitationRequest.getUserId())) {
            throw new InvalidInputException("no access");
        }
        boolean isChecklist = addUserToNoteOrChecklist(invitationRequest.getUserId(), invitationRequest.getItemId());
        return isChecklist ?
                getNoteResponseChecklist(invitationRequest, invitation)
                :
                getNoteResponseNote(invitationRequest, invitation);
    }

    private GetNoteResponse getNoteResponseChecklist(InvitationRequest invitationRequest, Invitation invitation) {
        ChecklistEntity checklist = checklistRepo.findById(UUID.fromString(invitationRequest.getItemId())).get();
        GetNoteResponse getNoteResponse = new GetNoteResponse(
            invitationRequest.getItemId(),
            checklist.getTitle(),
            checklist.getItemList(),
            checklist
                    .getUsers().stream().map(user -> user.getId()).collect(Collectors.toList()),
            false);
        invitationRepo.deleteById(UUID.fromString(invitationRequest.getItemId()));
        return getNoteResponse;
    }

    private GetNoteResponse getNoteResponseNote(InvitationRequest invitationRequest, Invitation invitation) {
        NoteEntity note = noteRepo.findById(UUID.fromString(invitationRequest.getItemId())).get();

        GetNoteResponse getNoteResponse =  new GetNoteResponse(
            invitationRequest.getItemId(),
            invitation.getTitle(),
            note.getContent(),
            note.getUsers()
                    .stream().map(user -> user.getId()).collect(Collectors.toList()),
            false);
        invitationRepo.deleteById(UUID.fromString(invitationRequest.getItemId()));
        return getNoteResponse;
    }

    private boolean addUserToNoteOrChecklist(String userId, String itemId) {
        UserEntity user = userRepo.findById(UUID.fromString(userId)).get();
        if(noteRepo.existsById(UUID.fromString(itemId))) {
            NoteEntity note = noteRepo.findById(UUID.fromString(itemId)).get();
            note.getUsers().add(user);
            noteRepo.save(note);
            return false;
        } else if(checklistRepo.existsById(UUID.fromString(itemId))) {
            ChecklistEntity checklist = checklistRepo.findById(UUID.fromString(itemId)).get();
            checklist.getUsers().add(user);
            checklistRepo.save(checklist);
            return true;
        } else {
            throw new EntityNotFoundException("Not found");
        }
    }

    public String declineNote(DeclineNoteRequest request) {
        System.out.println("!!!! request" + request.getRequestId() + " !!item " + request.getItemId() + " !!user " + request.getUserId());

        verifyUserInput.verifyIfAcceptNoteQueryExists(request.getRequestId());
        verifyUserInput.verifyThatRecipientAndQueryMatch(request);
        invitationRepo.deleteById(UUID.fromString(request.getRequestId()));
        return "invitation declined";
    }
}
