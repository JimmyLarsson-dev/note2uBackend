package com.example.note2ubackendnosecurity.checklist;

import lombok.Data;
import java.util.UUID;

@Data
public class ItemDTO {

    private UUID id;
    private String title;
    private String jobIsDone;
}
