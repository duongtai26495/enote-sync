package com.kai.mynote.entities;

import com.kai.mynote.util.AppConstants;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "active_codes")
public class ActiveCode {

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
