package com.example.note2ubackendnosecurity.notes;

import com.example.note2ubackendnosecurity.other.NoteAccessMissingException;
import com.example.note2ubackendnosecurity.other.NoteMissingException;
import com.example.note2ubackendnosecurity.other.UserMissingException;
import com.example.note2ubackendnosecurity.user.UserEntity;
import com.example.note2ubackendnosecurity.user.UserRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NoteService {

    private final NoteRepo noteRepo;
    private final UserRepo userRepo;

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

    public List<GetNoteResponse> getAllMyNotes(String id) {

        Optional<UserEntity> optionalUser = userRepo.findById(UUID.fromString(id));
        List<GetNoteResponse> dtoList;

        if(optionalUser.isPresent()) {
            dtoList = optionalUser.get().getNotes()
                    .stream()
                    .map(x -> entityToDto(x)).toList();
        } else {
            dtoList = new ArrayList<>();
        }
        return dtoList;
    }

    public String inviteUser(InvitationRequest request) {

        //lägg till regex för att kolla email
        if(!request.getRecipientEmail().isEmpty()) {
            Optional<UserEntity> optUserRecipient = userRepo.findByEmail(request.getRecipientEmail());

            if(optUserRecipient.isPresent()) {
                List<UserEntity> blockedList = optUserRecipient.get().getBlockedUsers()
                        .stream()
                        .filter(x -> x.getId().toString().equals(request.getInviterId()))
                        .toList();
                if(!blockedList.isEmpty()) {
                    return "blocked";
                }
            } else {
                return "emailNotFound";
            }




        }

        return "";

    }

    private GetNoteResponse entityToDto(NoteEntity note) {
        return new GetNoteResponse(
                note.getId(),
                note.getTitle(),
                note.getContent(),
                note.getUsers());
    }
}
