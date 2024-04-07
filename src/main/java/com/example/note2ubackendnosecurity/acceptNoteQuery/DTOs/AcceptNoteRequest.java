package com.example.note2ubackendnosecurity.acceptNoteQuery.DTOs;

import lombok.Data;

@Data
public class AcceptNoteRequest {

    private String NoteId;
    private String userId;
}
