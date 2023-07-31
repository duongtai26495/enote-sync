package com.kai.mynote.service;

import com.kai.mynote.dto.UserDTO;
import com.kai.mynote.dto.UserRegisterDTO;
import com.kai.mynote.dto.UserUpdateDTO;
import com.kai.mynote.entities.User;

public interface UserService {
    UserDTO createUser(UserRegisterDTO userRegisterDTO);

    User updateUser(UserUpdateDTO updateDTO);

    UserDTO getUserByUsername(String username);

    boolean isExistByEmail(String email);

    boolean isExistByUsername(String username);
}
