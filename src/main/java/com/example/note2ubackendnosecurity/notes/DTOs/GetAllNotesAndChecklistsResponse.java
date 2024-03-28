package com.example.note2ubackendnosecurity.notes.DTOs;

import com.example.note2ubackendnosecurity.checklist.ChecklistResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetAllNotesAndChecklistsResponse {

    public List<ChecklistResponse> checklistList;
    public List<GetNoteResponse> noteList;

}
