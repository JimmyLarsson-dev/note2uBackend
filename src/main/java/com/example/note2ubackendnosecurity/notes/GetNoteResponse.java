package com.example.note2ubackendnosecurity.notes;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class GetNoteResponse {

    private UUID id;
    private String title;
    private String content;

}
