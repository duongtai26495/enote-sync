package com.kai.mynote.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Tasks")
@Data
public class Task {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(length = 512)
    private String content;

    private Type type;

    private boolean done = false;

    private boolean enabled = true;

    @ManyToOne
    @JoinColumn(name = "note_id")
    @JsonBackReference
    private Note note;


    @ManyToOne
    @JoinColumn(name = "author_id")
    @JsonIgnore
    private User author;
}
