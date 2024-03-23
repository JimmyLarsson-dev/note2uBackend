package com.example.note2ubackendnosecurity.notes.DTOs;

import lombok.Data;

@Data
public class InvitationRequest {

    private String inviterId;
    private String recipientUsername;
    private String recipientEmail;
    private String noteId;
}
