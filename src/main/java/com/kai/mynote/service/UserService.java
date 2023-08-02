package com.kai.mynote.service;

import com.kai.mynote.dto.UserDTO;
import com.kai.mynote.dto.UserRegisterDTO;
import com.kai.mynote.dto.UserUpdateDTO;
import com.kai.mynote.entities.Blacklist;
import com.kai.mynote.entities.Note;
import com.kai.mynote.entities.User;
import com.kai.mynote.entities.WorkSpace;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface UserService {
    UserDTO createUser(UserRegisterDTO userRegisterDTO);

    User updateUser(UserUpdateDTO updateDTO);

    UserDTO getUserByUsername(String username);

    boolean isExistByEmail(String email);

    boolean isExistByUsername(String username);

    Page<WorkSpace> getAllWorkspace(String username, int pageNo, int pageSize);

    User getUserForAuthor(String username);

    void addTokenToBlacklist(String username, String token);

    Blacklist checkTokenInBlacklist(String username, String token);
}
