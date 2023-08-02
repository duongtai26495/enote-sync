package com.kai.mynote.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "blacklist")
@Data
public class Blacklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
