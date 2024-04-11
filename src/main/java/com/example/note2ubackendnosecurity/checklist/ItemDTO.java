package com.example.note2ubackendnosecurity.checklist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {

    private UUID id;
    private String title;
    private String jobIsDone;
}
