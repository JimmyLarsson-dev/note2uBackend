package com.example.note2ubackendnosecurity.notes.DTOs;

import lombok.Data;

@Data
public class CreateNoteRequest {
    String title;
    String content;
    String userId;
}
