package com.example.note2ubackendnosecurity.checklist;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Data
public class UpdateChecklistRequest {

    private String userId;
    private String checklistId;
    private String title;
    private List<ItemDTO> itemList;

}
