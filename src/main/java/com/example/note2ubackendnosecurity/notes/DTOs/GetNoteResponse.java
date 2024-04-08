package com.example.note2ubackendnosecurity.notes.DTOs;

import com.example.note2ubackendnosecurity.checklist.Item;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class GetNoteResponse {
    private String itemId;
    private String title;
    private String content;
    private List<Item> itemlist;
    private List<UUID> users;
    private boolean hasBeenViewed;

    public GetNoteResponse(String itemId,
                           String title,
                           List<Item> itemlist,
                           List<UUID> users,
                           boolean hasBeenViewed) {
        this.itemId = itemId;
        this.title = title;
        this.itemlist = itemlist;
        this.users = users;
        this.hasBeenViewed = hasBeenViewed;
    }

    public GetNoteResponse(String itemId,
                           String title,
                           String content,
                           List<UUID> users,
                           boolean hasBeenViewed) {
        this.itemId = itemId;
        this.title = title;
        this.content = content;
        this.users = users;
        this.hasBeenViewed = hasBeenViewed;
    }
}




