package com.example.note2ubackendnosecurity.notes.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetAllNotesAndChecklistsResponse {

    public List<GetNoteResponse> noteList;

}
