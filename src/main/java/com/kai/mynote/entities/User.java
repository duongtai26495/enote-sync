package com.kai.mynote.entities;


import com.kai.mynote.dto.UserDTO;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    private boolean enabled = true;

    public UserDTO convertDTO(User user){
        return new UserDTO(user.getId(), user.getF_name(), user.getL_name(), user.getEmail(), user.getUsername(), user.isEnabled());
    }
}
