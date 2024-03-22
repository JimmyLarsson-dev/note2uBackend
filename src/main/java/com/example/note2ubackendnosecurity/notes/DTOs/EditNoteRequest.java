package com.example.note2ubackendnosecurity.notes.DTOs;

import lombok.Data;

@Data
public class EditNoteRequest {
    String noteId;
    String userId;
    String title;
    String content;
}
