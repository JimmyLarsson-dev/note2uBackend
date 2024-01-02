package com.example.note2ubackendnosecurity.checklist;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
public class Item {

    @Id
    private UUID id;
    private String title;
    private boolean isDone;

    public Item() {
    }
}
