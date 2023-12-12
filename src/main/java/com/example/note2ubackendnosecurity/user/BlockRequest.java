package com.example.note2ubackendnosecurity.user;

import lombok.Data;

@Data
public class BlockRequest {

    private String callingUserId;
    private String blockedUserEmail;

}
