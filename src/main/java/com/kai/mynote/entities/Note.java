package com.kai.mynote.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "note")
@Data
public class Note {

    @Id
    @Column(name = "note_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 250)
    private String name;

    private boolean isDone = false;

    private boolean enabled = true;

    private double progress = 0.0;

    @Column(length = 1000)
    private String featured_image = "";

    @ManyToOne
    @JoinColumn(name = "workspace_id")
    private WorkSpace workspace;

    @OneToMany(mappedBy = "note")
    private List<Task> tasks;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @JsonIgnore
    private User author;

}
