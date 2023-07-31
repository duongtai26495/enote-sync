package com.kai.mynote.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workspace")
@Data
public class WorkSpace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 100)
    private String featured_image;

    @OneToMany(mappedBy = "workspace")
    private List<Note> notes;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

}
