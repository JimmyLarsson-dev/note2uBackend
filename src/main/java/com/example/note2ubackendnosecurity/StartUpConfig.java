package com.example.note2ubackendnosecurity;

import com.example.note2ubackendnosecurity.checklist.ChecklistEntity;
import com.example.note2ubackendnosecurity.checklist.ChecklistRepo;
import com.example.note2ubackendnosecurity.checklist.Item;
import com.example.note2ubackendnosecurity.checklist.UserViewedMap;
import com.example.note2ubackendnosecurity.notes.NoteEntity;
import com.example.note2ubackendnosecurity.notes.NoteRepo;
import com.example.note2ubackendnosecurity.user.UserEntity;
import com.example.note2ubackendnosecurity.user.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Configuration
public class StartUpConfig {

    //Den här klassen används för att skapa några poster i databasen för att
    // ha något att jobba med under utvecklingen

    private final UserRepo userRepo;
    private final NoteRepo noteRepo;
    private final ChecklistRepo checklistRepo;

    public StartUpConfig(UserRepo userRepo, NoteRepo noteRepo, ChecklistRepo checklistRepo) {
        this.userRepo = userRepo;
        this.noteRepo = noteRepo;
        this.checklistRepo = checklistRepo;
    }

    @Bean
    public void autoCreateUsers() {
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

        Item item1 = new Item(UUID.randomUUID(), "buy apples", false);
        Item item2 = new Item(UUID.randomUUID(), "clean living room", false);
        Item item3 = new Item(UUID.randomUUID(), "shave cat", false);


        List<Item> itemList1 = List.of(item1, item2, item3);
        List<Item> itemList2 = List.of(item2, item3, item1);
        List<Item> itemList3 = List.of(item3, item1);

        UserViewedMap userViewedMap1 = new UserViewedMap(user1.getId(), false);
        UserViewedMap userViewedMap2 = new UserViewedMap(user2.getId(), false);
        UserViewedMap userViewedMap3 = new UserViewedMap(user1.getId(), false);

        ChecklistEntity checklist1 = new ChecklistEntity(
                UUID.randomUUID(),
                "Initial checklist 1",
                itemList1,
                List.of(user1),
                userViewedMap1);
        ChecklistEntity checklist2 = new ChecklistEntity(
                UUID.randomUUID(),
                "initial checklist 2",
                itemList2,
                List.of(user1),
                userViewedMap2);
        ChecklistEntity checklist3 = new ChecklistEntity(
                UUID.randomUUID(),
                "initial checklist 3",
                itemList3,
                List.of(user1),
                userViewedMap3);

        checklist1.getHasBeenViewed().setUserId(user1.getId());
        checklist1.getHasBeenViewed().setNoteOrChecklistId(checklist1.getId());
        checklist1.getHasBeenViewed().setHasUserViewed(false);
        checklist1.getHasBeenViewed().setMapId(UUID.randomUUID());

        checklist2.getHasBeenViewed().setUserId(user1.getId());
        checklist2.getHasBeenViewed().setNoteOrChecklistId(checklist2.getId());
        checklist2.getHasBeenViewed().setHasUserViewed(false);
        checklist2.getHasBeenViewed().setMapId(UUID.randomUUID());

        checklist3.getHasBeenViewed().setUserId(user1.getId());
        checklist3.getHasBeenViewed().setNoteOrChecklistId(checklist3.getId());
        checklist3.getHasBeenViewed().setHasUserViewed(false);
        checklist3.getHasBeenViewed().setMapId(UUID.randomUUID());

        userRepo.save(user1);
        userRepo.save(user2);
        userRepo.save(user3);
        userRepo.save(user4);
        userRepo.save(user5);

        checklistRepo.save(checklist1);
        checklistRepo.save(checklist2);
        checklistRepo.save(checklist3);

    }
}
