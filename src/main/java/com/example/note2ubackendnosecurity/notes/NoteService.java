package com.example.note2ubackendnosecurity.notes;

import com.example.note2ubackendnosecurity.checklist.ChecklistResponse;
import com.example.note2ubackendnosecurity.exceptions.InvalidInputException;
import com.example.note2ubackendnosecurity.exceptions.NoteAccessMissingException;
import com.example.note2ubackendnosecurity.exceptions.NoteMissingException;
import com.example.note2ubackendnosecurity.exceptions.UserMissingException;
import com.example.note2ubackendnosecurity.notes.DTOs.*;
import com.example.note2ubackendnosecurity.user.UserEntity;
import com.example.note2ubackendnosecurity.user.UserRepo;
import com.example.note2ubackendnosecurity.utilities.CheckUserInput;
import com.example.note2ubackendnosecurity.utilities.EntityToDtoConverter;
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
    private final CheckUserInput checkUserInput;
    private final EntityToDtoConverter entityToDtoConverter;

    public NoteService(NoteRepo noteRepo,
                       UserRepo userRepo,
                       CheckUserInput checkUserInput,
                       EntityToDtoConverter entityToDtoConverter) {
        this.noteRepo = noteRepo;
        this.userRepo = userRepo;
        this.checkUserInput = checkUserInput;
        this.entityToDtoConverter = entityToDtoConverter;
    }

    public String createNote(CreateNoteRequest request) throws UserMissingException {
        checkUserInput.checkIfUserExists(request.getUserId());
        Optional<UserEntity> user = userRepo.findById(UUID.fromString(request.getUserId()));
        if (request.getTitle() == null) {
            request.setTitle(" ");
        }
        return createNoteAndReturnNoteId(request, user);
    }

    private String createNoteAndReturnNoteId(CreateNoteRequest request, Optional<UserEntity> user) {
        NoteEntity note = new NoteEntity(request.getTitle(), request.getContent(), user.get(), false);
        noteRepo.save(note);
        return note.getId().toString();
    }

    public String editNote(EditNoteRequest request) throws NoteAccessMissingException, NoteMissingException {
        Optional<NoteEntity> optNote = noteRepo.findById(UUID.fromString(request.getNoteId()));
        checkUserInput.checkIfNoteExists(request.getNoteId());
        checkUserInput.checkUserHasAccess(request.getUserId(), request.getNoteId());
        optNote.get().setTitle(request.getTitle());
        optNote.get().setContent(request.getContent());
        noteRepo.save(optNote.get());
        return "Note updated!";
    }

    public String deleteNote(String noteId, String userId) throws NoteAccessMissingException, NoteMissingException {
        checkUserInput.checkIfNoteExists(noteId);
        checkUserInput.checkUserHasAccess(userId, noteId);
        noteRepo.delete(noteRepo.getReferenceById(UUID.fromString(noteId)));
        return "Note deleted!";
    }

    public GetNoteResponse getNote(GetNoteRequest getNoteRequest) throws NoteMissingException, NoteAccessMissingException {
        Optional<NoteEntity> optionalNote = noteRepo.findById(UUID.fromString(getNoteRequest.getNoteId()));
        checkUserInput.checkIfNoteExists(getNoteRequest.getNoteId());
        checkUserInput.checkUserHasAccess(getNoteRequest.getUserId(), getNoteRequest.getNoteId());
        return entityToDtoConverter.convertNoteToGetNoteResponse(optionalNote.get());
    }

    public List<GetNoteResponse> getAllMyNotes(String userId) throws UserMissingException {
        checkUserInput.checkIfUserExists(userId);
        Optional<UserEntity> optionalUser = userRepo.findById(UUID.fromString(userId));
        return entityToDtoConverter.getNoteResponseListFromUserId(optionalUser);
    }

    public String inviteUserByEmail(InvitationRequest request) throws UserMissingException, NoteMissingException {
        checkUserInput.checkIfUserExists(request.getInviterId());
        checkUserInput.checkIfNoteExists(request.getNoteId());
        checkUserInput.checkEmailFormat(request.getRecipientEmail());

        Optional<UserEntity> recipientUser = userRepo.findByEmail(request.getRecipientEmail());
        if (recipientUser.isEmpty()) {
            throw new UserMissingException("Recipient not found!");
        }

        List<UserEntity> blockedList = recipientUser.get().getBlockedUsers()
                .stream()
                .filter(x -> x.getId().toString().equals(request.getInviterId()))
                .toList();
        if (!blockedList.isEmpty()) {
            return "blocked";
        }

        //här borde en förfrågan gå ut till mottagaren, istället för att bara lägga till note.
        recipientUser.get().getNotes().add(noteRepo.findById(UUID.fromString(request.getNoteId())).get());
        NoteEntity note = noteRepo.findById(UUID.fromString(request.getNoteId())).get();
        note.getUsers().add(recipientUser.get());
        return "Note sent!";
    }

    public String inviteUserByUsername(InvitationRequest request) throws UserMissingException, NoteMissingException {
        checkUserInput.checkIfUserExists(request.getInviterId());
        checkUserInput.checkIfNoteExists(request.getNoteId());

//Lägg till regex för att kolla username
        if (!request.getRecipientUsername().isEmpty()) {
            Optional<UserEntity> optUserRecipient = userRepo.findByUsername(request.getRecipientEmail());

            if (optUserRecipient.isPresent()) {
                List<UserEntity> blockedList = optUserRecipient.get().getBlockedUsers()
                        .stream()
                        .filter(x -> x.getId().toString().equals(request.getInviterId()))
                        .toList();
                if (!blockedList.isEmpty()) {
                    return "blocked";
                } else {
                    //här borde en förfrågan gå ut till mottagaren, istället för att bara lägga till note.
                    optUserRecipient.get().getNotes().add(noteRepo.findById(UUID.fromString(request.getNoteId())).get());
                    NoteEntity note = noteRepo.findById(UUID.fromString(request.getNoteId())).get();
                    note.getUsers().add(optUserRecipient.get());
                    return "Note sent!";
                }
            } else {
                throw new UserMissingException("Recipient not found");
            }
        } else {
            throw new InvalidInputException("Invalid username format");
        }
    }



    public GetAllNotesAndChecklistsResponse getAllMyNotesAndChecklists(String id) throws UserMissingException {

        Optional<UserEntity> optionalUser = userRepo.findById(UUID.fromString(id));
        if (optionalUser.isEmpty()) {
            throw new UserMissingException("No such user!");
        }
        List<GetNoteResponse> getNoteList = new ArrayList<>();
        List<ChecklistResponse> getCheckListList = new ArrayList<>();


        if (!optionalUser.get().getNotes().isEmpty()) {
//                optionalUser.get().getNotes()
//                        .forEach(x -> dtoList.add(new GetNoteResponse(x.getId(), x.getTitle(), x.getContent(), x.getUsers())));
            for (int i = 0; i < optionalUser.get().getNotes().size(); i++) {
                getNoteList.add(new GetNoteResponse(optionalUser.get().getNotes().get(i).getId(),
                        optionalUser.get().getNotes().get(i).getTitle(),
                        optionalUser.get().getNotes().get(i).getContent(),
                        optionalUser.get().getNotes().get(i).getUsers().stream().map(x -> x.getId()).collect(Collectors.toList()),
                        optionalUser.get().getNotes().get(i).isStatusBeenViewed()
                ));
            }
        }
        if (!optionalUser.get().getCheckLists().isEmpty()) {
            for (int i = 0; i < optionalUser.get().getCheckLists().size(); i++) {
                getCheckListList.add(
                        new ChecklistResponse(
                                optionalUser.get().getCheckLists().get(i).getId().toString(),
                                optionalUser.get().getCheckLists().get(i).getTitle(),
                                optionalUser.get().getCheckLists().get(i).getItemList(),
                                optionalUser.get().getCheckLists().get(i).getUsers(),
                                optionalUser.get().getCheckLists().get(i).getHasBeenViewed()
                        ));
            }
        }
        return new GetAllNotesAndChecklistsResponse(getCheckListList, getNoteList);

    }
}
