package com.example.note2ubackendnosecurity.user.DTOs;

import lombok.Data;

@Data
public class BlockRequest {

    private String callingUserId;
    private String blockedUserEmail;
    private String blockedUserUsername;
}
