package com.example.note2ubackendnosecurity.utilities;

import com.example.note2ubackendnosecurity.notes.DTOs.GetNoteResponse;
import com.example.note2ubackendnosecurity.notes.NoteEntity;
import com.example.note2ubackendnosecurity.user.DTOs.UserInNoteOrChecklist;
import com.example.note2ubackendnosecurity.user.UserEntity;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EntityToDtoConverter {

    public List<GetNoteResponse> getNoteResponseListFromUserId(Optional<UserEntity> optionalUser) {
        List<GetNoteResponse> dtoList = new ArrayList<>();
        if (!optionalUser.get().getNotes().isEmpty()) {
            for (int i = 0; i < optionalUser.get().getNotes().size(); i++) {
                dtoList.add(new GetNoteResponse(
                        optionalUser.get().getNotes().get(i).getId().toString(),
                        optionalUser.get().getNotes().get(i).getTitle(),
                        optionalUser.get().getNotes().get(i).getContent(),
                        optionalUser.get().getNotes().get(i).getUsers().stream().map(x -> x.getId()).collect(Collectors.toList()),
                        optionalUser.get().getNotes().get(i).isStatusBeenViewed()
                ));
            }
        }
        return dtoList;
    }

    public GetNoteResponse convertNoteToGetNoteResponse(NoteEntity note) {
        return new GetNoteResponse(
                note.getId().toString(),
                note.getTitle(),
                note.getContent(),
                note.getUsers()
                        .stream()
                        .map(x -> x.getId())
                        .collect(Collectors.toList()),
                note.isStatusBeenViewed());
    }

    public UserInNoteOrChecklist convertUserEntityToUserInNoteOrChecklist(UserEntity user) {
        return new UserInNoteOrChecklist(
                user.getId().toString(),
                user.getEmail(),
                user.getUsername());
    }
}
