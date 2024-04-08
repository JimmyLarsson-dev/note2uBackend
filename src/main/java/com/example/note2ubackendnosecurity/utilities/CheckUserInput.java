package com.example.note2ubackendnosecurity.utilities;

import com.example.note2ubackendnosecurity.acceptNoteQuery.AcceptNoteQuery;
import com.example.note2ubackendnosecurity.acceptNoteQuery.AcceptNoteQueryRepo;
import com.example.note2ubackendnosecurity.exceptions.InvalidInputException;
import com.example.note2ubackendnosecurity.exceptions.NoteAccessMissingException;
import com.example.note2ubackendnosecurity.exceptions.NoteMissingException;
import com.example.note2ubackendnosecurity.exceptions.UserMissingException;
import com.example.note2ubackendnosecurity.notes.NoteRepo;
import com.example.note2ubackendnosecurity.user.UserEntity;
import com.example.note2ubackendnosecurity.user.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CheckUserInput {

    private final NoteRepo noteRepo;
    private final UserRepo userRepo;
    private final AcceptNoteQueryRepo acceptNoteQueryRepo;

    public CheckUserInput(NoteRepo noteRepo, UserRepo userRepo, AcceptNoteQueryRepo acceptNoteQueryRepo) {
        this.noteRepo = noteRepo;
        this.userRepo = userRepo;
        this.acceptNoteQueryRepo = acceptNoteQueryRepo;
    }

    public void checkEmailFormat(String email) {
        //Dubbelkolla regex!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        if(!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new InvalidInputException("Incorrect email format!");
        }
    }

    public void checkIfNoteExists(String noteId) throws NoteMissingException {
        if(!noteRepo.existsById(UUID.fromString(noteId))) {
            throw new NoteMissingException("No such note found!");
        }
    }

    public void checkUserHasAccess(String userId, String noteId) throws NoteAccessMissingException {
        if (!userRepo.existsByIdAndNotesContains(UUID.fromString(userId), noteRepo.getReferenceById(UUID.fromString(noteId)) )) {
            throw new NoteAccessMissingException("User does not have access to that note!");
        }
    }

    public void checkIfUserExists(String userId, String message) throws UserMissingException {
        if (userRepo.existsById(UUID.fromString(userId))) {
            throw new UserMissingException(message);
        }
    }

    public void checkIfUserExists(String userId) throws UserMissingException {
        if (userRepo.existsById(UUID.fromString(userId))) {
            throw new UserMissingException("User does not exist");
        }
    }

    public boolean checkIfSenderIsBlocked(String senderId, String recipientId) {
        List<UserEntity> blockedList = userRepo.findById(UUID.fromString(recipientId)).get().getBlockedUsers()
                .stream()
                .filter(x -> x.getId().toString().equals(senderId))
                .toList();
        //if the sender is blocked, return true
        return !blockedList.isEmpty();
    }

    public AcceptNoteQuery checkIfAcceptNoteQueryExists(String requestId) {
        Optional<AcceptNoteQuery> optionalAcceptNoteQuery = acceptNoteQueryRepo.findById(UUID.fromString(requestId));
        if(optionalAcceptNoteQuery.isEmpty()) {
            throw new EntityNotFoundException("no such request");
        }
        return optionalAcceptNoteQuery.get();
    }
}
