package com.example.note2ubackendnosecurity.notes.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateNoteRequest {
    String title;
    String content;
    String userId;
}
