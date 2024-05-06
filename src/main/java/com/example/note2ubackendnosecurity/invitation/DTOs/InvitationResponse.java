package com.example.note2ubackendnosecurity.invitation.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InvitationResponse {
    private String senderUsername;
    private String noteTitle;
    private String requestId;
    private String itemId;
}
