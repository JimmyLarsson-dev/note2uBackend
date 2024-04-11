package com.example.note2ubackendnosecurity.checklist.DTOs;

import com.example.note2ubackendnosecurity.checklist.Item;
import com.example.note2ubackendnosecurity.checklist.UserViewedMap;
import com.example.note2ubackendnosecurity.user.DTOs.UserInNoteOrChecklist;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class ChecklistResponse {
    private String checklistId;
    private String title;
    private List<Item> itemList;
    private List<UserInNoteOrChecklist> users;
    private UserViewedMap statusBeenViewed;
}

