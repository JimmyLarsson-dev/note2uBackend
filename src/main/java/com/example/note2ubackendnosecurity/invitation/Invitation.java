package com.example.note2ubackendnosecurity.invitation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="invitation")
public class Invitation {

    @Id
    private UUID requestId;
    private boolean itemIsChecklist;
    private UUID itemId; //Id:t på note eller checklist
    private UUID senderId;
    private UUID recipientId;
    private String title;

}
