package com.kai.mynote.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(length = 1024)
    private String featured_image = "";

    @OneToMany(mappedBy = "workspace")
    @JsonIgnore
    private List<Note> notes = new ArrayList<>();

    private int note_count;

    @Column(updatable = false)
    private String created_at;

    private String updated_at;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @JsonIgnore
    private User author;

    public int getNote_count() {
        return this.notes.size();
    }

}
