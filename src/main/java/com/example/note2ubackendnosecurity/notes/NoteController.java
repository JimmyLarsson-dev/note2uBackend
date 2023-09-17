package com.example.note2ubackendnosecurity.notes;

import com.example.note2ubackendnosecurity.other.NoteAccessMissingException;
import com.example.note2ubackendnosecurity.other.NoteMissingException;
import com.example.note2ubackendnosecurity.other.UserMissingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public String deleteNote(@RequestBody EditNoteRequest request) {
        return noteService.deleteNote(request.getNoteId(), request.getUserId(), request.getTitle(), request.getContent());
    }

}
