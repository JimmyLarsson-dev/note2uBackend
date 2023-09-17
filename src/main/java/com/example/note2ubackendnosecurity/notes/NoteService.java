package com.example.note2ubackendnosecurity.notes;

import com.example.note2ubackendnosecurity.other.UserMissingException;
import com.example.note2ubackendnosecurity.user.UserEntity;
import com.example.note2ubackendnosecurity.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            System.out.println("title: " + title);
            System.out.println("content: " + content);
            NoteEntity note = new NoteEntity(title, content, user.get());
            noteRepo.save(note);
            return "note created!";
        } else {
            throw new UserMissingException("No such user!");
        }


    }
}
