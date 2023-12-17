package com.example.note2ubackendnosecurity;

import com.example.note2ubackendnosecurity.notes.NoteEntity;
import com.example.note2ubackendnosecurity.notes.NoteRepo;
import com.example.note2ubackendnosecurity.user.UserEntity;
import com.example.note2ubackendnosecurity.user.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class StartUpConfig {

    private final UserRepo userRepo;
    private final NoteRepo noteRepo;

    public StartUpConfig(UserRepo userRepo, NoteRepo noteRepo) {
        this.userRepo = userRepo;
        this.noteRepo = noteRepo;
    }

    @Bean
    public CommandLineRunner autoCreateUsers() {
        NoteEntity note1 = new NoteEntity();
        UserEntity user1 = new UserEntity(
                "j@m.com",
                "j",
                "p",
                List.of(note1),
                List.of());
        note1.setUsers(List.of(user1));
        note1.setTitle("init note1");
        note1.setContent("This is the first note, initialized by Startupconfig.");

        NoteEntity note2 = new NoteEntity();
        UserEntity user2 = new UserEntity(
                "swimmy@mail.com",
                "swimbolino",
                "passpass",
                List.of(note2),
                List.of());
        note2.setUsers(List.of(user2));
        note2.setTitle("init note2");
        note2.setContent("This is the first note, initialized by Startupconfig.");
        noteRepo.save(note1);
        noteRepo.save(note2);

        return args -> {
            userRepo.save(user1);
            userRepo.save(user2);

        };
    }

}
