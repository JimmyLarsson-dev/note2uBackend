package com.example.note2ubackendnosecurity.acceptNoteQuery;

import com.example.note2ubackendnosecurity.acceptNoteQuery.DTOs.NoteQueryResponse;
import com.example.note2ubackendnosecurity.checklist.ChecklistRepo;
import com.example.note2ubackendnosecurity.exceptions.NoteMissingException;
import com.example.note2ubackendnosecurity.exceptions.UserMissingException;
import com.example.note2ubackendnosecurity.notes.NoteEntity;
import com.example.note2ubackendnosecurity.notes.NoteRepo;
import com.example.note2ubackendnosecurity.user.UserRepo;
import com.example.note2ubackendnosecurity.utilities.CheckUserInput;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AcceptNoteQueryService {

    private final CheckUserInput checkUserInput;
    private final AcceptNoteQueryRepo acceptNoteQueryRepo;
    private final NoteRepo noteRepo;
    private final ChecklistRepo checklistRepo;
    private final UserRepo userRepo;

    public AcceptNoteQueryService(
            CheckUserInput checkUserInput,
            AcceptNoteQueryRepo acceptNoteQueryRepo,
            NoteRepo noteRepo,
            ChecklistRepo checklistRepo,
            UserRepo userRepo) {
        this.checkUserInput = checkUserInput;
        this.acceptNoteQueryRepo = acceptNoteQueryRepo;
        this.noteRepo = noteRepo;
        this.checklistRepo = checklistRepo;
        this.userRepo = userRepo;
    }

    public NoteQueryResponse checkReceivedNotes(String userId) throws NoteMissingException, UserMissingException {
        Optional<AcceptNoteQuery> optionalAcceptNoteQuery = acceptNoteQueryRepo.findByRecipientId(UUID.fromString(userId));
        checkUserInput.checkIfUserExists(userId);
        if(optionalAcceptNoteQuery.isEmpty()) {
            throw new NoteMissingException("No new notes");
        }
        String senderUsername = userRepo.findById(optionalAcceptNoteQuery.get().getSenderId()).get().getUsername();
        String noteTitle = noteRepo.findById(optionalAcceptNoteQuery.get().getItemId()).get().getTitle();
        return new NoteQueryResponse(senderUsername, noteTitle);
    }
}
