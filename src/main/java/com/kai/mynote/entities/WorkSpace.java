package com.kai.mynote.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @Column(length = 250)
    private String name;

    @Column(length = 1000)
    private String featured_image = "";

    @OneToMany(mappedBy = "workspace")
    @JsonManagedReference
    private List<Note> notes;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @JsonBackReference
    private User author;

}
