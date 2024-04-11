package com.example.note2ubackendnosecurity.checklist.DTOs;

import lombok.Data;

@Data
public class GetChecklistRequest {

    private String userId;
    private String checklistId;

}
