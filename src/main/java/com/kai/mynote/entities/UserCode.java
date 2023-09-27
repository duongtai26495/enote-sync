package com.kai.mynote.entities;

import com.kai.mynote.enums.CodeTye;
import com.kai.mynote.util.AppConstants;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "user_code")
public class UserCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String username;

    @Column(length = 50)
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
