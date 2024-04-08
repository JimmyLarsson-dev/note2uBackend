package com.example.note2ubackendnosecurity.notes;

import com.example.note2ubackendnosecurity.exceptions.NoteAccessMissingException;
import com.example.note2ubackendnosecurity.exceptions.NoteMissingException;
import com.example.note2ubackendnosecurity.exceptions.UserMissingException;
import com.example.note2ubackendnosecurity.notes.DTOs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/note")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class NoteController {

    NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping("/createNote")
    public String createNote(@RequestBody CreateNoteRequest request) throws UserMissingException {
        return noteService.createNote(request);
    }

    @PutMapping("/editNote")
    public String editNote(@RequestBody EditNoteRequest request) throws NoteAccessMissingException, NoteMissingException {
        return noteService.editNote(request);
    }

    @DeleteMapping("/deleteNote")
    public String deleteNote(@RequestBody EditNoteRequest request) throws NoteMissingException, NoteAccessMissingException {
        return noteService.deleteNote(request.getNoteId(), request.getUserId());
    }

    @GetMapping("/getNote")
    public GetNoteResponse getNote(@RequestBody GetNoteRequest getNoteRequest) throws NoteMissingException, NoteAccessMissingException {
        return noteService.getNote(getNoteRequest);
    }

    @GetMapping("/getAllMyNotes")
    public List<GetNoteResponse> getAllMyNotes(@RequestHeader String userId) throws UserMissingException {
        return noteService.getAllMyNotes(userId);
    }

    @GetMapping("/getAllMyNotesAndChecklists")
    public List<GetNoteResponse> getAllMyNotesAndChecklists(@RequestHeader String id) throws UserMissingException {
        return noteService.getAllMyNotesAndChecklists(id);
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
