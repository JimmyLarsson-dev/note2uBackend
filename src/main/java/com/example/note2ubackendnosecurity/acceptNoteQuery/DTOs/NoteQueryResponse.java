package com.example.note2ubackendnosecurity.acceptNoteQuery.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NoteQueryResponse {
    private String senderUsername;
    private String noteTitle;
}
