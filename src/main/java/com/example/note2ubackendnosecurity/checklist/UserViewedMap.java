package com.example.note2ubackendnosecurity.checklist;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserViewedMap {

    @Id
    private UUID mapId;
    private UUID noteOrChecklistId;
    private UUID userId;
    private boolean hasUserViewed;

    //den här konstruktorn är bara för Startupconfig
    public UserViewedMap(UUID userId, boolean hasUserViewed) {
        this.userId = userId;
        this.hasUserViewed = hasUserViewed;
    }
}
