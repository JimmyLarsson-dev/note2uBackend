package com.example.note2ubackendnosecurity.notes;

import lombok.Data;

@Data
public class InvitationRequest {

    private String inviterId;
    private String recipientId;
    private String recipientEmail;
    private String noteId;
}
