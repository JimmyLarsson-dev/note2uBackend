package com.example.note2ubackendnosecurity.acceptNoteQuery.DTOs;

import lombok.Data;

@Data
public class AcceptNoteRequest {

    private String requestId;
    private String itemId;
    private String userId;
}
