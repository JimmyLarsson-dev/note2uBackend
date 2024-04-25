package com.example.note2ubackendnosecurity.checklist.DTOs;

import com.example.note2ubackendnosecurity.checklist.ItemDTO;
import lombok.Data;
import java.util.List;

@Data
public class UpdateChecklistRequest {

    private String userId;
    private String checklistId;
    private String title;
    private List<ItemDTO> itemList;

}
