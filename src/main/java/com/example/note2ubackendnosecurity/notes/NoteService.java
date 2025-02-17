package com.example.note2ubackendnosecurity.notes;

import com.example.note2ubackendnosecurity.checklist.DTOs.ChecklistResponse;
import com.example.note2ubackendnosecurity.exceptions.NoteAccessMissingException;
import com.example.note2ubackendnosecurity.exceptions.NoteMissingException;
import com.example.note2ubackendnosecurity.exceptions.UserMissingException;
import com.example.note2ubackendnosecurity.notes.DTOs.*;
import com.example.note2ubackendnosecurity.user.UserEntity;
import com.example.note2ubackendnosecurity.user.UserRepo;
import com.example.note2ubackendnosecurity.utilities.InvitationsHandler;
import com.example.note2ubackendnosecurity.utilities.VerifyUserInput;
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
    private final VerifyUserInput verifyUserInput;
    private final EntityToDtoConverter entityToDtoConverter;
    private final InvitationsHandler invitationsHandler;

    public NoteService(NoteRepo noteRepo,
                       UserRepo userRepo,
                       VerifyUserInput verifyUserInput,
                       EntityToDtoConverter entityToDtoConverter,
                       InvitationsHandler invitationsHandler) {
        this.noteRepo = noteRepo;
        this.userRepo = userRepo;
        this.verifyUserInput = verifyUserInput;
        this.entityToDtoConverter = entityToDtoConverter;
        this.invitationsHandler = invitationsHandler;
    }

    public String createNote(CreateNoteRequest request) throws UserMissingException {
        verifyUserInput.verifyIfUserExists(request.getUserId());
        Optional<UserEntity> user = userRepo.findById(UUID.fromString(request.getUserId()));
        if (request.getTitle() == null) {
            request.setTitle("New note");
        }
        return createNoteAndReturnNoteId(request, user);
    }

    private String createNoteAndReturnNoteId(CreateNoteRequest request, Optional<UserEntity> user) {
        NoteEntity note = new NoteEntity(request.getTitle(), request.getContent(), false, List.of(user.get()));
        noteRepo.save(note);
        return note.getId().toString();
    }

    public String editNote(EditNoteRequest request) throws NoteAccessMissingException, NoteMissingException {
        Optional<NoteEntity> optNote = noteRepo.findById(UUID.fromString(request.getNoteId()));
        verifyUserInput.verifyIfNoteExists(request.getNoteId());
        verifyUserInput.verifyUserHasAccess(request.getUserId(), request.getNoteId());
        optNote.get().setTitle(request.getTitle());
        optNote.get().setContent(request.getContent());
        noteRepo.save(optNote.get());
        return "Note updated!";
    }

    public String deleteNote(EditNoteRequest request) throws NoteAccessMissingException, NoteMissingException {
        verifyUserInput.verifyIfNoteExists(request.getNoteId());
        verifyUserInput.verifyUserHasAccess(request.getUserId(), request.getNoteId());
        noteRepo.delete(noteRepo.getReferenceById(UUID.fromString(request.getNoteId())));
        return "Note deleted!";
    }

    public GetNoteResponse getNote(GetNoteRequest getNoteRequest) throws NoteMissingException, NoteAccessMissingException {
        Optional<NoteEntity> optionalNote = noteRepo.findById(UUID.fromString(getNoteRequest.getNoteId()));
        verifyUserInput.verifyIfNoteExists(getNoteRequest.getNoteId());
        verifyUserInput.verifyUserHasAccess(getNoteRequest.getUserId(), getNoteRequest.getNoteId());
        return entityToDtoConverter.convertNoteToGetNoteResponse(optionalNote.get());
    }

    public List<GetNoteResponse> getAllMyNotes(String userId) throws UserMissingException {
        verifyUserInput.verifyIfUserExists(userId);
        Optional<UserEntity> optionalUser = userRepo.findById(UUID.fromString(userId));
        return entityToDtoConverter.getNoteResponseListFromUserId(optionalUser);
    }

    public String inviteUser(InvitationRequest request) throws UserMissingException, NoteMissingException {
        verifyUserInput.verifyIfUserExists(request.getInviterId(), "Inviting user does not exist");
        if (!request.getRecipientEmail().isEmpty()) {
            return invitationsHandler.inviteByEmail(request);
        } else if (!request.getRecipientUsername().isEmpty()) {
            return invitationsHandler.inviteByUsername(request);
        } else {
            return "no recipient";
        }
    }

    public List<GetNoteResponse> getAllMyNotesAndChecklists(String userId) throws UserMissingException {
        Optional<UserEntity> optionalUser = userRepo.findById(UUID.fromString(userId));
        verifyUserInput.verifyIfUserExists(userId);
        List<GetNoteResponse> getNoteList = new ArrayList<>();
        List<ChecklistResponse> getCheckListList = new ArrayList<>();
        if (!optionalUser.get().getNotes().isEmpty()) {
            for (int i = 0; i < optionalUser.get().getNotes().size(); i++) {
                getNoteList.add(new GetNoteResponse(
                        optionalUser.get().getNotes().get(i).getId().toString(),
                        optionalUser.get().getNotes().get(i).getTitle(),
                        optionalUser.get().getNotes().get(i).getContent(),
                        optionalUser.get().getNotes().get(i).getUsers().stream().map(x -> x.getId()).collect(Collectors.toList()),
                        optionalUser.get().getNotes().get(i).isStatusBeenViewed()
                ));
            }
        }
        if (!optionalUser.get().getCheckLists().isEmpty()) {
            for (int i = 0; i < optionalUser.get().getCheckLists().size(); i++) {
                getNoteList.add(
                        new GetNoteResponse(
                                optionalUser.get().getCheckLists().get(i).getId().toString(),
                                optionalUser.get().getCheckLists().get(i).getTitle(),
                                optionalUser.get().getCheckLists().get(i).getItemList(),
                                optionalUser.get().getCheckLists().get(i).getUsers().stream().map(x -> x.getId()).collect(Collectors.toList()),
                                optionalUser.get().getCheckLists().get(i).getHasBeenViewed().isHasUserViewed()
                        ));
            }
        }
        return getNoteList;

    }
}
