package com.example.note2ubackendnosecurity.notes.DTOs;

import lombok.Data;

@Data
public class NoteContent {

    private String item;
    private boolean hasBeenViewed;
}
