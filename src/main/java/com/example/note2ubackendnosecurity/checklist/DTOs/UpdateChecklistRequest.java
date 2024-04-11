package com.example.note2ubackendnosecurity.checklist.DTOs;

import com.example.note2ubackendnosecurity.checklist.ItemDTO;
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
