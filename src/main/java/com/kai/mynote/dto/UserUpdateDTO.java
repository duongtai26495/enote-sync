package com.kai.mynote.dto;

import com.kai.mynote.entities.Gender;
import lombok.Data;

@Data
public class UserUpdateDTO {
    private String f_name;

    private String l_name;

    private String username;

    private Gender gender;

    private String password;

    private String profile_image;
}
