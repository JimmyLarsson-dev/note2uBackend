package com.example.note2ubackendnosecurity.notes;

import com.example.note2ubackendnosecurity.other.UserMissingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/note")
public class NoteController {

    NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping("/createNote")
    public String createNote(@RequestBody String title, String content, String userId) throws UserMissingException {
        return noteService.createNote(title, content, userId);
    }

}
