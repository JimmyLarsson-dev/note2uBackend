package com.example.note2ubackendnosecurity.invitation.DTOs;

import lombok.Data;

@Data
public class DeclineNoteRequest {

    private String requestId;
    private String itemId;
    private String userId;
}
