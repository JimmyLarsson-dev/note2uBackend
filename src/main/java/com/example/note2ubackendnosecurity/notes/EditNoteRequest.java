package com.example.note2ubackendnosecurity.notes;

import lombok.Data;

@Data
public class EditNoteRequest {
    private String noteId;
    private String userId;
    private String title;
    private String content;
}
