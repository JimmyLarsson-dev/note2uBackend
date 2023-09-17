package com.example.note2ubackendnosecurity.notes;

import lombok.Data;

@Data
public class CreateNoteRequest {
    private String userId;
    private String title;
    private String content;
}
