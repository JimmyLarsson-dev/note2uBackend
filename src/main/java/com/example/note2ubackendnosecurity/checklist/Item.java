package com.example.note2ubackendnosecurity.checklist;

import lombok.Data;

import java.util.UUID;

@Data
public class Item {

    private String title;
    private boolean isDone;

}
