package com.example.note2ubackendnosecurity.notes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteService {


    private final NoteRepo noteRepo;

    @Autowired
    public NoteService(NoteRepo noteRepo) {
        this.noteRepo = noteRepo;
    }

    public String createNote(String title, String content) {

        if(title == null) {
            title = " ";
        }
        NoteEntity note = new NoteEntity(title, content);

        noteRepo.save(note);

        return "note created!";
    }

}
