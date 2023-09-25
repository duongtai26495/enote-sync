package com.kai.mynote.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    public Role(String role_name) {
        this.role_name = role_name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long role_id;

    @Column(length = 20)
    private String role_name;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private List<User> users;
}
