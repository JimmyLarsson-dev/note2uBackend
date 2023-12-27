package com.example.note2ubackendnosecurity.checklist;

import com.example.note2ubackendnosecurity.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "checklist")
public class ChecklistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name= "checklist_id")
    UUID id;
    private String title;

//    @ManyToMany()
//    @Cascade({org.hibernate.annotations.CascadeType.REFRESH, org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.PERSIST})
//    private List<Item> itemList;
    @ManyToMany
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "userId")
    private List<UserEntity> users;
    private boolean statusBeenViewed;


    public ChecklistEntity(/*UUID id,*/
                           String title,
/*
                           List<Item> itemList,
*/
                           UserEntity user,
                           boolean statusHasBeenViewed) {
        /*this.id = id;*/
        this.title = title;
        /*this.itemList = itemList;*/
        this.users = List.of(user);
        this.statusBeenViewed = statusHasBeenViewed;

    }

}
