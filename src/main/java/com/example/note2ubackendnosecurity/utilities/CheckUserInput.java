package com.example.note2ubackendnosecurity.utilities;

import com.example.note2ubackendnosecurity.exceptions.InvalidInputException;
import com.example.note2ubackendnosecurity.exceptions.NoteAccessMissingException;
import com.example.note2ubackendnosecurity.exceptions.NoteMissingException;
import com.example.note2ubackendnosecurity.exceptions.UserMissingException;
import com.example.note2ubackendnosecurity.notes.NoteRepo;
import com.example.note2ubackendnosecurity.user.UserRepo;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class CheckUserInput {

    private final NoteRepo noteRepo;
    private final UserRepo userRepo;

    public CheckUserInput(NoteRepo noteRepo, UserRepo userRepo) {
        this.noteRepo = noteRepo;
        this.userRepo = userRepo;
    }

    public void checkEmailFormat(String email) {
        //Dubbelkolla regex!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        if(!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new InvalidInputException("Incorrect email format!");
        }
    }

    public void checkIfNoteExists(String noteId) throws NoteMissingException {
        if(!noteRepo.existsById(UUID.fromString(noteId))) {
            throw new NoteMissingException("No such note found!");
        }
    }

    public void checkNoteExistsAndUserHasAccess(String userId, String noteId) throws NoteMissingException, NoteAccessMissingException {
        checkIfNoteExists(noteId);
        if (!userRepo.existsByIdAndNotesContains(UUID.fromString(userId), noteRepo.getReferenceById(UUID.fromString(noteId)) )) {
            throw new NoteAccessMissingException("User does not have access to that note!");
        }
    }

    public void checkIfUserExists(String userId) throws UserMissingException {
        if (userRepo.existsById(UUID.fromString(userId))) {
            throw new UserMissingException("No such user!");
        }
    }
}

