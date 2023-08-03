package com.kai.mynote.dto;


import com.kai.mynote.entities.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;

    private String f_name;

    private String l_name;

    private String email;

    private String username;

    private Gender gender;

    private boolean enabled;
}
