package com.example.note2ubackendnosecurity.checklist;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChecklistResponse {
    private String id;
    private String title;
}

