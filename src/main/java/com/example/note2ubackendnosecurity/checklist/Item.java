package com.example.note2ubackendnosecurity.checklist;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class Item {

    @Id
    private UUID id;
    private String title;
    private boolean jobIsDone;

    public Item(UUID id, String title, boolean jobIsDone) {
        this.id = id;
        this.title = title;
        this.jobIsDone = jobIsDone;
    }
}
