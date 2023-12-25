package com.example.note2ubackendnosecurity.checklist;

import com.example.note2ubackendnosecurity.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import java.util.List;
import java.util.UUID;

@Entity
//@AllArgsConstructor
//@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Table(name = "checklist")
public class ChecklistEntity {


//    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name= "checklist_id")
    private Long id;
    private String title;
    @ManyToMany()
    @Cascade({org.hibernate.annotations.CascadeType.REFRESH, org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.PERSIST})
    private List<Item> itemList;
    @ManyToMany
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.PERSIST})
    @JoinColumn(name = "userId")
    private List<UserEntity> users;
    private boolean statusBeenViewed;


    public ChecklistEntity(Long id,
                           String title,
                           List<Item> itemList,
                           UserEntity user,
                           boolean statusHasBeenViewed) {
        this.id = id;
        this.title = title;
        this.itemList = itemList;
        this.users = List.of(user);
        this.statusBeenViewed = statusHasBeenViewed;

    }

}
