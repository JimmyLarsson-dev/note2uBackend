package com.example.note2ubackendnosecurity.checklist;

import com.example.note2ubackendnosecurity.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
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
    @JoinTable(
            name = "checklist_item",
            joinColumns = @JoinColumn(name = "checklist_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> itemList;

    @ManyToMany
    @JoinTable(
            name = "checklist_user",
            joinColumns = @JoinColumn(name = "checklist_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> users;

    @OneToOne
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
