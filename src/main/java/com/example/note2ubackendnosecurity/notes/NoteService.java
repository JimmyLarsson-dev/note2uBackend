package com.example.note2ubackendnosecurity.notes;

import com.example.note2ubackendnosecurity.acceptNoteQuery.AcceptNoteQuery;
import com.example.note2ubackendnosecurity.acceptNoteQuery.AcceptNoteQueryRepo;
import com.example.note2ubackendnosecurity.checklist.DTOs.ChecklistResponse;
import com.example.note2ubackendnosecurity.exceptions.InvalidInputException;
import com.example.note2ubackendnosecurity.exceptions.NoteAccessMissingException;
import com.example.note2ubackendnosecurity.exceptions.NoteMissingException;
import com.example.note2ubackendnosecurity.exceptions.UserMissingException;
import com.example.note2ubackendnosecurity.notes.DTOs.*;
import com.example.note2ubackendnosecurity.user.UserEntity;
import com.example.note2ubackendnosecurity.user.UserRepo;
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
    private final AcceptNoteQueryRepo acceptNoteQueryRepo;

    public NoteService(NoteRepo noteRepo,
                       UserRepo userRepo,
                       VerifyUserInput verifyUserInput,
                       EntityToDtoConverter entityToDtoConverter,
                       AcceptNoteQueryRepo acceptNoteQueryRepo) {
        this.noteRepo = noteRepo;
        this.userRepo = userRepo;
        this.verifyUserInput = verifyUserInput;
        this.entityToDtoConverter = entityToDtoConverter;
        this.acceptNoteQueryRepo = acceptNoteQueryRepo;
    }

    public String createNote(CreateNoteRequest request) throws UserMissingException {
        verifyUserInput.verifyIfUserExists(request.getUserId());
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

    public String inviteUserByEmail(InvitationRequest request) throws UserMissingException, NoteMissingException {
        verifyUserInput.verifyIfUserExists(request.getInviterId(), "Inviting user does not exist");
        verifyUserInput.verifyIfNoteExists(request.getNoteId());
        verifyUserInput.verifyEmailFormat(request.getRecipientEmail());

        Optional<UserEntity> recipientUser = userRepo.findByEmail(request.getRecipientEmail());
        if (recipientUser.isEmpty()) {
            throw new UserMissingException("Recipient not found!");
        }

        if(verifyUserInput.verifyIfSenderIsBlocked(
                request.getInviterId(),
                recipientUser.get().getId().toString())) {
            return "blocked";
        }

        AcceptNoteQuery acceptNoteQuery = new AcceptNoteQuery(
                UUID.randomUUID(),
                UUID.fromString(request.getNoteId()),
                UUID.fromString(request.getInviterId()),
                recipientUser.get().getId(),
                request.getTitle());
        acceptNoteQueryRepo.save(acceptNoteQuery);

        return "Note sent!";
    }

    public String inviteUserByUsername(InvitationRequest request) throws UserMissingException, NoteMissingException {
        verifyUserInput.verifyIfUserExists(request.getInviterId(), "Inviting user does not exist");
        verifyUserInput.verifyIfNoteExists(request.getNoteId());

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



    public List<GetNoteResponse> getAllMyNotesAndChecklists(String userId) throws UserMissingException {
        Optional<UserEntity> optionalUser = userRepo.findById(UUID.fromString(userId));

        verifyUserInput.verifyIfUserExists(userId);

        List<GetNoteResponse> getNoteList = new ArrayList<>();
        List<ChecklistResponse> getCheckListList = new ArrayList<>();


        if (!optionalUser.get().getNotes().isEmpty()) {
//                optionalUser.get().getNotes()
//                        .forEach(x -> dtoList.add(new GetNoteResponse(x.getId(), x.getTitle(), x.getContent(), x.getUsers())));
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
