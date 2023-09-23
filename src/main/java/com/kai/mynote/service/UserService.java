package com.kai.mynote.service;

import com.kai.mynote.dto.UserDTO;
import com.kai.mynote.dto.UserRegisterDTO;
import com.kai.mynote.dto.UserUpdateDTO;
import com.kai.mynote.entities.Blacklist;
import com.kai.mynote.entities.User;
import com.kai.mynote.entities.WorkSpace;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;

import java.util.HashMap;

public interface UserService {
    User createUser(UserRegisterDTO userRegisterDTO) throws MessagingException;

    User updateUser(User user);

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    boolean isExistByEmail(String email);

    boolean isExistByUsername(String username);

    Page<WorkSpace> getAllWorkspace(String username, int pageNo, int pageSize, String sort);

    User getUserForAuthor(String username);

    void addTokenToBlacklist(String username, String token);

    Blacklist checkTokenInBlacklist(String username, String token);

    void updatePassword(User user);

    HashMap<String, String> userAnalytics(String username);

    void setActivateUser(String username, boolean activate);

    void sendActivateMail(User user);

    void sendRecoveryPwMail(User user);
}
