//package com.example.note2ubackendnosecurity;
//
//import com.example.note2ubackendnosecurity.checklist.ChecklistEntity;
//import com.example.note2ubackendnosecurity.checklist.ChecklistRepo;
//import com.example.note2ubackendnosecurity.checklist.Item;
//import com.example.note2ubackendnosecurity.checklist.UserViewedMap;
//import com.example.note2ubackendnosecurity.notes.NoteEntity;
//import com.example.note2ubackendnosecurity.notes.NoteRepo;
//import com.example.note2ubackendnosecurity.user.UserEntity;
//import com.example.note2ubackendnosecurity.user.UserRepo;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.List;
//import java.util.UUID;
//
//@Configuration
//public class StartUpConfig {

    //Den här klassen används för att skapa några poster i databasen för att
    // ha något att jobba med under utvecklingen

//    private final UserRepo userRepo;
//    private final NoteRepo noteRepo;
//    private final ChecklistRepo checklistRepo;
//    private final PasswordEncoder passwordEncoder;
//
//    UUID UUID1 = UUID.fromString("9cbb5e51-76d6-4d3b-b60e-5c99e1767d13");
//    UUID UUID2 = UUID.fromString("fd9ef357-1552-4b3a-a8e6-17b0b5409aa0");
//    UUID UUID3 = UUID.fromString("8b26cf6d-4b87-4c3d-b861-2a5797bca1d9");
//    UUID UUID4 = UUID.fromString("57917a38-af4e-4897-aa58-940f5b0d7784");
//    UUID UUID5 = UUID.fromString("c98bf3f7-4b35-4f9f-82df-6f4a6b7f7e2c");
//
//    public StartUpConfig(UserRepo userRepo, NoteRepo noteRepo, ChecklistRepo checklistRepo, PasswordEncoder passwordEncoder) {
//        this.userRepo = userRepo;
//        this.noteRepo = noteRepo;
//        this.checklistRepo = checklistRepo;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @Bean
//    public void autoCreateUsers() {
//        NoteEntity note1 = new NoteEntity();
//        UserEntity user1 = UserEntity.builder()
//                .id(UUID.randomUUID())
//                .email("j@m.com")
//                .username("j")
//                .password(passwordEncoder.encode("p"))
//                .notes(List.of())
//                .checkLists(List.of())
//                .language("swedish")
//                .blockedUsers(List.of())
//                .build();
//
//        userRepo.save(user1);
//        note1.setId(UUID1);
//        note1.setUsers(List.of(user1));
//        note1.setTitle("init note1");
//        note1.setContent("Note1, initialized by Startupconfig.");
//        note1.setStatusBeenViewed(false);
//        noteRepo.save(note1);
//        user1.getNotes().add(noteRepo.findById(UUID1).get());
//        userRepo.save(user1);

//        NoteEntity note2 = new NoteEntity();
//        UserEntity user2 = UserEntity.builder()
//                .id(UUID.randomUUID())
//                .email("jimmy@mail.com")
//                .username("jimmy")
//                .password(passwordEncoder.encode("password"))
//                .notes(List.of())
//                .checkLists(List.of())
//                .language("swedish")
//                .blockedUsers(List.of())
//                .build();
//        userRepo.save(user2);
//        note2.setUsers(List.of(user2));
//        note2.setTitle("init note2");
//        note2.setContent("Note2, initialized by Startupconfig.");
//        note2.setStatusBeenViewed(false);
//        noteRepo.save(note2);
//        user2.getNotes().add(note2);
//        userRepo.save(user2);
//
//        NoteEntity note3 = new NoteEntity();
//        UserEntity user3 = UserEntity.builder()
//                .id(UUID.randomUUID())
//                .email("swimmy@email.se")
//                .username("swimmy")
//                .password(passwordEncoder.encode("password"))
//                .notes(List.of())
//                .checkLists(List.of())
//                .language("swedish")
//                .blockedUsers(List.of())
//                .build();
//        userRepo.save(user3);
//        note3.setUsers(List.of(user3));
//        note3.setTitle("init note3");
//        note3.setContent("Note3, initialized by Startupconfig.");
//        note3.setStatusBeenViewed(false);
//        noteRepo.save(note3);
//        user3.getNotes().add(note3);
//        userRepo.save(user3);
//
//        NoteEntity note4 = new NoteEntity();
//        UserEntity user4 = UserEntity.builder()
//                .id(UUID.randomUUID())
//                .email("pimmy@brev.se")
//                .username("pimmy")
//                .password(passwordEncoder.encode("password"))
//                .notes(List.of())
//                .checkLists(List.of())
//                .language("english")
//                .blockedUsers(List.of())
//                .build();
//        userRepo.save(user4);
//        note4.setUsers(List.of(user4));
//        note4.setTitle("init note4");
//        note4.setContent("Note4, initialized by Startupconfig.");
//        note4.setStatusBeenViewed(false);
//        noteRepo.save(note4);
//        user4.getNotes().add(note4);
//        userRepo.save(user4);
//
//        NoteEntity note5 = new NoteEntity();
//        UserEntity user5 = UserEntity.builder()
//                .id(UUID.randomUUID())
//                .email("plimmy@myMail.com")
//                .username("plimmy")
//                .password(passwordEncoder.encode("password"))
//                .notes(List.of())
//                .checkLists(List.of())
//                .language("english")
//                .blockedUsers(List.of())
//                .build();
//        userRepo.save(user5);
//        note5.setUsers(List.of(user5));
//        note5.setTitle("init note5");
//        note5.setContent("Note5, initialized by Startupconfig.");
//        note5.setStatusBeenViewed(false);
//        noteRepo.save(note5);
//        user5.getNotes().add(note5);
//        userRepo.save(user5);
//
////        noteRepo.save(note1);
////        noteRepo.save(note2);
////        noteRepo.save(note3);
////        noteRepo.save(note4);
////        noteRepo.save(note5);
//
//        Item item1 = new Item(UUID.randomUUID(), "buy apples", false);
//        Item item2 = new Item(UUID.randomUUID(), "clean living room", false);
//        Item item3 = new Item(UUID.randomUUID(), "shave cat", false);
//
//        List<Item> itemList1 = List.of(item1, item2, item3);
//        List<Item> itemList2 = List.of(item2, item3, item1);
//        List<Item> itemList3 = List.of(item3, item1);
//
//        UserViewedMap userViewedMap1 = new UserViewedMap(
//                UUID.randomUUID(),
//                null,
//                user1.getId(),
//                true);
//        UserViewedMap userViewedMap2 = new UserViewedMap(
//                UUID.randomUUID(),
//                null,
//                user2.getId(),
//                true);
//        UserViewedMap userViewedMap3 = new UserViewedMap(
//                UUID.randomUUID(),
//                null,
//                user1.getId(),
//                false);
//
//        ChecklistEntity checklist1 = new ChecklistEntity(
//                UUID.randomUUID(),
//                "Initial checklist 1",
//                itemList1,
//                List.of(user1),
//                userViewedMap1);
//        ChecklistEntity checklist2 = new ChecklistEntity(
//                UUID.randomUUID(),
//                "initial checklist 2",
//                itemList2,
//                List.of(user1),
//                userViewedMap2);
//        ChecklistEntity checklist3 = new ChecklistEntity(
//                UUID.randomUUID(),
//                "initial checklist 3",
//                itemList3,
//                List.of(user1),
//                userViewedMap3);
//
//        checklist1.getHasBeenViewed().setUserId(user1.getId());
//        checklist1.getHasBeenViewed().setNoteOrChecklistId(checklist1.getId());
//        checklist1.getHasBeenViewed().setHasUserViewed(false);
//        checklist1.getHasBeenViewed().setMapId(UUID.randomUUID());
//
//        checklist2.getHasBeenViewed().setUserId(user1.getId());
//        checklist2.getHasBeenViewed().setNoteOrChecklistId(checklist2.getId());
//        checklist2.getHasBeenViewed().setHasUserViewed(false);
//        checklist2.getHasBeenViewed().setMapId(UUID.randomUUID());
//
//        checklist3.getHasBeenViewed().setUserId(user1.getId());
//        checklist3.getHasBeenViewed().setNoteOrChecklistId(checklist3.getId());
//        checklist3.getHasBeenViewed().setHasUserViewed(false);
//        checklist3.getHasBeenViewed().setMapId(UUID.randomUUID());
//
//        userViewedMap1.setNoteOrChecklistId(checklist1.getId());
//        userViewedMap2.setNoteOrChecklistId(checklist2.getId());
//        userViewedMap3.setNoteOrChecklistId(checklist3.getId());
//
////        userRepo.save(user1);
////        userRepo.save(user2);
////        userRepo.save(user3);
////        userRepo.save(user4);
////        userRepo.save(user5);
//
//        checklistRepo.save(checklist1);
//        checklistRepo.save(checklist2);
//        checklistRepo.save(checklist3);

//    }
//}
