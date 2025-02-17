package com.example.note2ubackendnosecurity.checklist;

import com.example.note2ubackendnosecurity.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "checklist")
public class ChecklistEntity {

    @Id
    @Column(name= "checklist_id")
    UUID id;
    private String title;

    @ManyToMany
    @JoinColumn(name = "item_id")
    @Cascade({CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST})
    private List<Item> itemList;

    @ManyToMany
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "userId")
    private List<UserEntity> users;

    @OneToOne
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "mapId")
    private UserViewedMap hasBeenViewed;

    public ChecklistEntity(UUID id,
                           String title,
                           List<Item> itemList,
                           List<UserEntity> users,
                           UserViewedMap hasBeenViewed) {
        this.id = id;
        this.title = title;
        this.itemList = itemList;
        this.users = users;
        this.hasBeenViewed = hasBeenViewed;
    }
}
