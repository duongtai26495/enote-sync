package com.kai.mynote.entities;

import com.kai.mynote.util.AppConstants;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "activate_codes")
public class ActivateCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    @Column(length = AppConstants.CODE_LENGTH)
    private String code;

    @Column(updatable = false)
    private Date expiredAt;

    @Column(updatable = false)
    private Date createdAt;

    @Enumerated(EnumType.STRING)
    private CodeTye type;

    private boolean isUsed = false;
}
