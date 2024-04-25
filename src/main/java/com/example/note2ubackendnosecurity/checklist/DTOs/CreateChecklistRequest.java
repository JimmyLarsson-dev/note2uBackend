package com.example.note2ubackendnosecurity.checklist.DTOs;

import lombok.Data;
import java.util.List;

@Data
public class CreateChecklistRequest {

    private String userId;
    private String title;
    private List<ItemRequest> itemList;
}
