package com.example.note2ubackendnosecurity.checklist;

import com.example.note2ubackendnosecurity.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ChecklistResponse {
    private String id;
    private String title;
    private List<Item> itemList;
    private List<UserEntity> users;
    private boolean statusBeenViewed;
}

