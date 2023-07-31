package com.kai.mynote.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "note")
@Data
public class Note {

    @Id
    @Column(name = "note_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 5000)
    private String content;

    private boolean isDone;


    @Column(length = 1000)
    private String featured_image;

    @ManyToOne
    @JoinColumn(name = "workspace_id")
    private WorkSpace workspace;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
}
