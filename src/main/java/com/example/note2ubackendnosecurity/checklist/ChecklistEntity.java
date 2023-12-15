package com.example.note2ubackendnosecurity.checklist;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "checklist")
public class ChecklistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name= "checklist_id")
    private UUID id;
    @ManyToMany()
    @Cascade({org.hibernate.annotations.CascadeType.REFRESH, org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.PERSIST})
////    @JoinColumn(name ="item")
    private List<Item> itemList;
}
