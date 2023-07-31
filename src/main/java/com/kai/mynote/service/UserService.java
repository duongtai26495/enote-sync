package com.kai.mynote.service;

import com.kai.mynote.dto.UserDTO;
import com.kai.mynote.dto.UserRegisterDTO;
import com.kai.mynote.entities.User;

public interface UserService {
    UserDTO createUser(UserRegisterDTO userRegisterDTO);

    UserDTO updateUser(User user);

    UserDTO getUserByUsername(String username);

    boolean isExistByEmail(String email);

    boolean isExistByUsername(String username);
}
