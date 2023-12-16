package com.example.note2ubackendnosecurity.checklist;

import lombok.Data;

import java.util.List;

@Data
public class UpdateChecklistRequest {

    private String userId;
    private String checklistId;
    private String title;
    private List<Item> itemList;

}
