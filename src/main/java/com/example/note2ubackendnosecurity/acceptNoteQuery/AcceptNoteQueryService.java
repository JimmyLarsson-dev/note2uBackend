package com.example.note2ubackendnosecurity.acceptNoteQuery;

import com.example.note2ubackendnosecurity.acceptNoteQuery.DTOs.AcceptNoteRequest;
import com.example.note2ubackendnosecurity.acceptNoteQuery.DTOs.NoteQueryResponse;
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
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AcceptNoteQueryService {

    private final VerifyUserInput verifyUserInput;
    private final AcceptNoteQueryRepo acceptNoteQueryRepo;
    private final NoteRepo noteRepo;
    private final ChecklistRepo checklistRepo;
    private final UserRepo userRepo;

    public AcceptNoteQueryService(
            VerifyUserInput verifyUserInput,
            AcceptNoteQueryRepo acceptNoteQueryRepo,
            NoteRepo noteRepo,
            ChecklistRepo checklistRepo,
            UserRepo userRepo) {
        this.verifyUserInput = verifyUserInput;
        this.acceptNoteQueryRepo = acceptNoteQueryRepo;
        this.noteRepo = noteRepo;
        this.checklistRepo = checklistRepo;
        this.userRepo = userRepo;
    }

    public NoteQueryResponse checkReceivedNotes(String userId) throws NoteMissingException, UserMissingException {

        //What happens in this method if there is more than one received note??????????????????????????????????
        verifyUserInput.verifyIfUserExists(userId);
        Optional<AcceptNoteQuery> optionalAcceptNoteQuery = acceptNoteQueryRepo.findByRecipientId(UUID.fromString(userId));
        if(optionalAcceptNoteQuery.isEmpty()) {
            throw new NoteMissingException("No new notes");
        }
        String senderUsername = userRepo.findById(optionalAcceptNoteQuery.get().getSenderId()).get().getUsername();
        String noteTitle = noteRepo.findById(optionalAcceptNoteQuery.get().getItemId()).get().getTitle();
        return new NoteQueryResponse(senderUsername, noteTitle);
    }



    public GetNoteResponse acceptNote(AcceptNoteRequest acceptNoteRequest) throws UserMissingException {

//        Kolla att användaren finns
        verifyUserInput.verifyIfUserExists(acceptNoteRequest.getUserId());

//        kolla att queryn finns
        AcceptNoteQuery acceptNoteQuery =  verifyUserInput.verifyIfAcceptNoteQueryExists(acceptNoteRequest.getRequestId());

        //kolla att det är rätt användare som anropar
        if(!acceptNoteQuery.getRecipientId().toString().equals(acceptNoteRequest.getUserId())) {
            throw new InvalidInputException("no access");
        }

        //lägg till anropande användare i note/checklist userList
        boolean isChecklist = addUserToNoteOrChecklist(acceptNoteRequest.getUserId(), acceptNoteRequest.getItemId());

        return isChecklist ?
                getNoteResponseChecklist(acceptNoteRequest, acceptNoteQuery)
                :
                getNoteResponseNote(acceptNoteRequest, acceptNoteQuery);
    }

    private GetNoteResponse getNoteResponseChecklist(AcceptNoteRequest acceptNoteRequest, AcceptNoteQuery acceptNoteQuery) {
        return new GetNoteResponse(
                acceptNoteRequest.getItemId(),
                acceptNoteQuery.getTitle(),
                checklistRepo.findById(UUID.fromString(acceptNoteRequest.getItemId())).get().getItemList(),
                checklistRepo.findById(UUID.fromString(acceptNoteRequest.getItemId())).get().getUsers().stream().map(user -> user.getId()).collect(Collectors.toList()),
                false);
    }

    private GetNoteResponse getNoteResponseNote(AcceptNoteRequest acceptNoteRequest, AcceptNoteQuery acceptNoteQuery) {
        return new GetNoteResponse(
                acceptNoteRequest.getItemId(),
                acceptNoteQuery.getTitle(),
                noteRepo.findById(UUID.fromString(acceptNoteRequest.getItemId())).get().getContent(),
                noteRepo.findById(UUID.fromString(acceptNoteRequest.getItemId())).get().getUsers().stream().map(user -> user.getId()).collect(Collectors.toList()),
                false);
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
}
