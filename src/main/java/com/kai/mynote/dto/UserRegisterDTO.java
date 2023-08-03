package com.kai.mynote.dto;


import com.kai.mynote.entities.Gender;
import lombok.Data;

@Data
public class UserRegisterDTO {

    private String f_name;

    private String l_name;

    private String username;

    private String email;

    private String password;

    private Gender gender;
}
