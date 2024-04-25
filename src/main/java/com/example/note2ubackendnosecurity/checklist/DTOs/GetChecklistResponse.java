package com.example.note2ubackendnosecurity.checklist.DTOs;

import com.example.note2ubackendnosecurity.checklist.ItemDTO;
import com.example.note2ubackendnosecurity.user.DTOs.UserInNoteOrChecklist;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class GetChecklistResponse {
    String id;
    String title;
    List<ItemDTO> itemList;
    List<UserInNoteOrChecklist> users;
}
