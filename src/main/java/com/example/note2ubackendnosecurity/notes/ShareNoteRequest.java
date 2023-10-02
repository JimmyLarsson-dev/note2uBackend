package com.example.note2ubackendnosecurity.notes;

import lombok.Data;

@Data
public class ShareNoteRequest {

    private String sendingUserId;
    private String recipientUserId;
    private String noteId;

}
