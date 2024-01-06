package com.example.note2ubackendnosecurity;

import com.example.note2ubackendnosecurity.notes.NoteEntity;
import com.example.note2ubackendnosecurity.notes.NoteRepo;
import com.example.note2ubackendnosecurity.user.UserEntity;
import com.example.note2ubackendnosecurity.user.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import static com.example.note2ubackendnosecurity.other.Languages.*;

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
                List.of(),
                "SWEDISH");
        note1.setUsers(List.of(user1));
        note1.setTitle("init note1");
        note1.setContent("This is the first note, initialized by Startupconfig.");
        note1.setStatusBeenViewed(false);

        NoteEntity note2 = new NoteEntity();
        UserEntity user2 = new UserEntity(
                "swimmy@mail.com",
                "swimbolino",
                "passpass",
                List.of(note2),
                List.of(),
                "SWEDISH");
        note2.setUsers(List.of(user2));
        note2.setTitle("init note2");
        note2.setContent("This is the first note, initialized by Startupconfig.");
        note2.setStatusBeenViewed(false);

        NoteEntity note3 = new NoteEntity();
        UserEntity user3 = new UserEntity(
                "whatever@mail.com",
                "someone",
                "passpass",
                List.of(note3),
                List.of(),
                "SWEDISH");
        note3.setUsers(List.of(user3));
        note3.setTitle("init note3");
        note3.setContent("This is the first note, initialized by Startupconfig.");
        note3.setStatusBeenViewed(false);

        NoteEntity note4 = new NoteEntity();
        UserEntity user4 = new UserEntity(
                "whoever@mail.com",
                "person",
                "passpass",
                List.of(note4),
                List.of(),
                "ENGLISH");
        note4.setUsers(List.of(user4));
        note4.setTitle("init note4");
        note4.setContent("This is the first note, initialized by Startupconfig.");
        note4.setStatusBeenViewed(false);

        NoteEntity note5 = new NoteEntity();
        UserEntity user5 = new UserEntity(
                "someMail@mail.com",
                "user42",
                "passpass",
                List.of(note5),
                List.of(),
                "ENGLISH");
        note5.setUsers(List.of(user5));
        note5.setTitle("init note5");
        note5.setContent("This is the first note, initialized by Startupconfig.");
        note5.setStatusBeenViewed(false);

        noteRepo.save(note1);
        noteRepo.save(note2);
        noteRepo.save(note3);
        noteRepo.save(note4);
        noteRepo.save(note5);

        return args -> {
            userRepo.save(user1);
            userRepo.save(user2);
            userRepo.save(user3);
            userRepo.save(user4);
            userRepo.save(user5);
        };
    }
}
