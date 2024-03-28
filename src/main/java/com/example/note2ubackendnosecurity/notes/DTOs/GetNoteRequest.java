package com.example.note2ubackendnosecurity.notes.DTOs;

import lombok.Data;

@Data
public class GetNoteRequest {

    private String userId;
    private String noteId;

}
