package com.kai.enote.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "Role")
@Data
@Getter
@Setter
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id")
    private Integer roleId;

    private String authority;


    public Role(){
        super();
    }

    public Role(Integer roleId, String authority){
        this.roleId = roleId;
        this.authority = authority;
    }


    @Override
    public String getAuthority() {
        return this.authority;
    }

}
