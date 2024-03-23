package com.example.note2ubackendnosecurity.notes.DTOs;

import lombok.Data;

@Data
public class ShareNoteRequest {

    private String sendingUserId;
    private String recipientUserId;
    private String noteId;

}
