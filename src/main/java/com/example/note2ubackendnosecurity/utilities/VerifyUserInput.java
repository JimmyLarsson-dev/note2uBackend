package com.example.note2ubackendnosecurity.utilities;

import com.example.note2ubackendnosecurity.acceptNoteQuery.AcceptNoteQuery;
import com.example.note2ubackendnosecurity.acceptNoteQuery.AcceptNoteQueryRepo;
import com.example.note2ubackendnosecurity.checklist.ChecklistRepo;
import com.example.note2ubackendnosecurity.exceptions.InvalidInputException;
import com.example.note2ubackendnosecurity.exceptions.NoteAccessMissingException;
import com.example.note2ubackendnosecurity.exceptions.NoteMissingException;
import com.example.note2ubackendnosecurity.exceptions.UserMissingException;
import com.example.note2ubackendnosecurity.notes.NoteRepo;
import com.example.note2ubackendnosecurity.user.DTOs.BlockRequest;
import com.example.note2ubackendnosecurity.user.UserEntity;
import com.example.note2ubackendnosecurity.user.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VerifyUserInput {

    private final NoteRepo noteRepo;
    private final UserRepo userRepo;
    private final ChecklistRepo checklistRepo;
    private final AcceptNoteQueryRepo acceptNoteQueryRepo;

    public VerifyUserInput(NoteRepo noteRepo, UserRepo userRepo, ChecklistRepo checklistRepo, AcceptNoteQueryRepo acceptNoteQueryRepo) {
        this.noteRepo = noteRepo;
        this.userRepo = userRepo;
        this.checklistRepo = checklistRepo;
        this.acceptNoteQueryRepo = acceptNoteQueryRepo;
    }

    public void verifyEmailFormat(String email) {
        //Dubbelkolla regex!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        if(!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new InvalidInputException("Incorrect email format!");
        }
    }

    public void verifyIfNoteExists(String noteId) throws NoteMissingException {
        if(!noteRepo.existsById(UUID.fromString(noteId))) {
            throw new NoteMissingException("No such note found!");
        }
    }

    public void verifyIfChecklistExists(String checklistID) throws NoteMissingException {
        if(!checklistRepo.existsById(UUID.fromString(checklistID))) {
            throw new NoteMissingException("No such checklist found!");
        }
    }

    public void verifyUserHasAccess(String userId, String noteId) throws NoteAccessMissingException {
        if (!userRepo.existsByIdAndNotesContains(UUID.fromString(userId), noteRepo.getReferenceById(UUID.fromString(noteId)) )) {
            throw new NoteAccessMissingException("User does not have access to that note!");
        }
    }

    public void verifyIfUserExists(String userId, String message) throws UserMissingException {
        if (!userRepo.existsById(UUID.fromString(userId))) {
            throw new UserMissingException(message);
        }
    }

    public void verifyIfUserExists(String userId) throws UserMissingException {
        if (!userRepo.existsById(UUID.fromString(userId))) {
            throw new UserMissingException("User does not exist");
        }
    }

    public boolean verifyIfSenderIsBlocked(String senderId, String recipientId) {
        List<UserEntity> blockedList = userRepo.findById(UUID.fromString(recipientId)).get().getBlockedUsers()
                .stream()
                .filter(x -> x.getId().toString().equals(senderId))
                .toList();
        //if the sender is blocked, return true
        return !blockedList.isEmpty();
    }

    public AcceptNoteQuery verifyIfAcceptNoteQueryExists(String requestId) {
        Optional<AcceptNoteQuery> optionalAcceptNoteQuery = acceptNoteQueryRepo.findById(UUID.fromString(requestId));
        if(optionalAcceptNoteQuery.isEmpty()) {
            throw new EntityNotFoundException("no such request");
        }
        return optionalAcceptNoteQuery.get();
    }

    public void verifyChecklistForUser(String checklistId, UserEntity user) {
        if(user.getCheckLists()
                .stream()
                .noneMatch(x -> x.getId().equals(UUID.fromString(checklistId)))
        ) {
            throw new InvalidInputException("No such checklist for this user");
        }
    }

    public UserEntity checkUsersExistAndReturnCallingUser(BlockRequest request) throws UserMissingException {
        if(userRepo.findById(UUID.fromString(request.getCallingUserId())).isEmpty()) {
            throw new UserMissingException("No such user found in database");
        }
        if(userRepo.findByEmail(request.getBlockedUserEmail()).isEmpty() &&
            userRepo.findByUsername(request.getBlockedUserUsername()).isEmpty()
        ) {
            throw new UserMissingException("Cannot find user, unable to change status.");
        }
        return userRepo.findById(UUID.fromString(request.getCallingUserId())).get();
    }
}
