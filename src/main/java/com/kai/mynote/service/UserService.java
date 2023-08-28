package com.kai.mynote.service;

import com.kai.mynote.dto.UserDTO;
import com.kai.mynote.dto.UserRegisterDTO;
import com.kai.mynote.dto.UserUpdateDTO;
import com.kai.mynote.entities.Blacklist;
import com.kai.mynote.entities.User;
import com.kai.mynote.entities.WorkSpace;
import org.springframework.data.domain.Page;

public interface UserService {
    UserDTO createUser(UserRegisterDTO userRegisterDTO);

    UserDTO updateUser(UserUpdateDTO updateDTO);

    UserDTO getUserByUsername(String username);

    boolean isExistByEmail(String email);

    boolean isExistByUsername(String username);

    Page<WorkSpace> getAllWorkspace(String username, int pageNo, int pageSize);

    User getUserForAuthor(String username);

    void addTokenToBlacklist(String username, String token);

    Blacklist checkTokenInBlacklist(String username, String token);

    UserDTO updatePassword(UserUpdateDTO updateDTO);
}
