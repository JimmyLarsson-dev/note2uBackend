package com.example.note2ubackendnosecurity.notes;

import com.example.note2ubackendnosecurity.other.NoteAccessMissingException;
import com.example.note2ubackendnosecurity.other.NoteMissingException;
import com.example.note2ubackendnosecurity.other.UserMissingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/note")
public class NoteController {

    NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping("/createNote")
    public String createNote(@RequestBody CreateNoteRequest request) throws UserMissingException {
        return noteService.createNote(request.getTitle(), request.getContent(), request.getUserId());
    }

    @PutMapping("/editNote")
    public String editNote(@RequestBody EditNoteRequest request) throws NoteAccessMissingException, NoteMissingException {
        return noteService.editNote(request.getNoteId(), request.getUserId(), request.getTitle(), request.getContent());
    }

    @DeleteMapping("/deleteNote")
    public String deleteNote(@RequestBody EditNoteRequest request) throws NoteMissingException, NoteAccessMissingException {
        return noteService.deleteNote(request.getNoteId(), request.getUserId(), request.getTitle(), request.getContent());
    }

    @GetMapping("/getNote")
    public GetNoteResponse getNote(@RequestBody GetNoteRequest getNoteRequest) throws NoteMissingException, NoteAccessMissingException {
        return noteService.getNote(getNoteRequest);
    }

    @GetMapping("/getAllMyNotes")
    public List<GetNoteResponse> getAllMyNotes(@RequestHeader String id) throws NoteMissingException, NoteAccessMissingException, UserMissingException {
        return noteService.getAllMyNotes(id);
    }

    @PatchMapping("/inviteUserByEmail")
    public String inviteUserByEmail(@RequestBody InvitationRequest request) throws UserMissingException, NoteMissingException {
        return noteService.inviteUserByEmail(request);
    }

    @PatchMapping("/inviteUserByUsername")
    public String inviteUserByUsername(@RequestBody InvitationRequest request) throws UserMissingException, NoteMissingException {
        return noteService.inviteUserByUsername(request);
    }
}
