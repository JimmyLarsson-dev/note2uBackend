package com.example.note2ubackendnosecurity.notes;

import com.example.note2ubackendnosecurity.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class GetNoteResponse {

    private UUID id;
    private String title;
    private String content;
    private List<UserEntity> users;


}
