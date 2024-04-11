package com.example.note2ubackendnosecurity.checklist.DTOs;

import lombok.Data;

@Data
public class ItemRequest {

    private String title;
    private String isDone;
}
