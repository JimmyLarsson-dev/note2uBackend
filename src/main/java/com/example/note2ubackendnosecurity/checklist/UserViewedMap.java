package com.example.note2ubackendnosecurity.checklist;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class UserViewedMap {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID mapId;
    private UUID userId;
    private boolean hasUserViewed;

    public UserViewedMap(UUID mapId, boolean hasUserViewed) {
        this.mapId = mapId;
        this.hasUserViewed = hasUserViewed;
    }
}
