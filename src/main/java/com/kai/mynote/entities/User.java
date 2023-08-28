package com.kai.mynote.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kai.mynote.dto.UserDTO;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "users")

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String f_name;

    private String l_name;

    private String email;

    private String username;

    private String password;

    private Gender gender = Gender.UNKNOWN;

    @Enumerated(EnumType.STRING)
    private Provider provider = Provider.LOCAL;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Blacklist> blacklists;

    @OneToMany(mappedBy = "author")
    private List<WorkSpace> workspaces;

    @OneToMany(mappedBy = "author")
    @JsonIgnore
    private List<Note> notes;

    @OneToMany(mappedBy = "author")
    @JsonIgnore
    private List<Task> tasks;

    @Column(updatable = false)
    private String joined_at;

    private String updated_at;

    @Column(length = 1000)
    private String profile_image;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();

    private boolean enabled = true;

    public UserDTO convertDTO(User user){
        return new UserDTO(user.getId(), user.getF_name(), user.getL_name(), user.getEmail(), user.getUsername(),  user.getGender(), user.isEnabled(), user.getUpdated_at(), user.getJoined_at(), user.getProfile_image());
    }
}
