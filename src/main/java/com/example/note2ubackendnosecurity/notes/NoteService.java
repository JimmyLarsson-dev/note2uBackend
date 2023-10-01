package com.example.note2ubackendnosecurity.notes;

import com.example.note2ubackendnosecurity.other.NoteAccessMissingException;
import com.example.note2ubackendnosecurity.other.NoteMissingException;
import com.example.note2ubackendnosecurity.other.UserMissingException;
import com.example.note2ubackendnosecurity.user.UserEntity;
import com.example.note2ubackendnosecurity.user.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Optional;
import java.util.UUID;

@Service
public class NoteService {

    private final NoteRepo noteRepo;
    private final UserRepo userRepo;

    @Autowired
    public NoteService(NoteRepo noteRepo, UserRepo userRepo) {
        this.noteRepo = noteRepo;
        this.userRepo = userRepo;
    }

    public String createNote(String title, String content, String userId) throws UserMissingException {
        Optional<UserEntity> user = userRepo.findById(UUID.fromString(userId));
        if(user.isPresent()) {
            if(title == null) {
                title = " ";
            }
            NoteEntity note = new NoteEntity(title, content, user.get());
            noteRepo.save(note);
            return note.getId().toString();
        } else {
            throw new UserMissingException("No such user!");
        }
    }

    public String editNote(String noteId, String userId, String title, String content) throws NoteAccessMissingException, NoteMissingException {
        Optional<NoteEntity> optNote = noteRepo.findById(UUID.fromString(noteId));
        if(optNote.isPresent()) {
            if(userRepo.existsByIdAndNotesContains(UUID.fromString(userId), optNote.get())) {
                optNote.get().setTitle(title);
                optNote.get().setContent(content);
                return "Note updated!";
            } else {
                throw new NoteAccessMissingException("User does not have access to that note!");
            }
        } else {
            throw new NoteMissingException("No such note found!");
        }
    }

    public String deleteNote(String noteId, String userId, String title, String content) throws NoteAccessMissingException, NoteMissingException {
        Optional<NoteEntity> optNote = noteRepo.findById(UUID.fromString(noteId));
        if(optNote.isPresent()) {
            if(userRepo.existsByIdAndNotesContains(UUID.fromString(userId), optNote.get())) {
                noteRepo.delete(optNote.get());
                return "Note deleted!";
            } else {
                throw new NoteAccessMissingException("User does not have access to that note!");
            }
        } else {
            throw new NoteMissingException("No such note found!");
        }
    }

    public GetNoteResponse getNote(GetNoteRequest getNoteRequest) throws NoteMissingException, NoteAccessMissingException {
        Optional<NoteEntity> optionalNote = noteRepo.findById(UUID.fromString(getNoteRequest.getNoteId()));

        if(optionalNote.isPresent()) {

            if(optionalNote.get().getUsers().contains(
                    userRepo.findById(UUID.fromString(getNoteRequest.getUserId())).get())) {
                return entityToDto(optionalNote.get());

            } else {
                throw new NoteAccessMissingException ("You do not have access to that note");
            }

        } else {
            throw new NoteMissingException("That note does not exist");
        }
    }

    private GetNoteResponse entityToDto(NoteEntity note) {
        return new GetNoteResponse(
                note.getId(),
                note.getTitle(),
                note.getContent());
    }

}
