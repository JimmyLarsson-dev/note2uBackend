package com.example.note2ubackendnosecurity.acceptNoteQuery;

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
@Table(name="SendNoteAcceptQuery")
public class AcceptNoteQuery {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID requestId;
    private UUID itemId; //Id:t på note eller checklist
    private UUID senderId;
    private UUID recipientId;

}
