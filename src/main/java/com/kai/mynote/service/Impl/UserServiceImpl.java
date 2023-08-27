package com.kai.mynote.service.Impl;

import com.kai.mynote.assets.AppConstants;
import com.kai.mynote.config.MyUserDetails;
import com.kai.mynote.dto.UserDTO;
import com.kai.mynote.dto.UserRegisterDTO;
import com.kai.mynote.dto.UserUpdateDTO;
import com.kai.mynote.entities.*;
import com.kai.mynote.repository.*;
import com.kai.mynote.service.UserService;
import com.kai.mynote.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.kai.mynote.assets.AppConstants.TIME_FORMAT;


@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private BlacklistRepository blacklistRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public UserDTO createUser(UserRegisterDTO userRegisterDTO) {
        User user = new User();
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);
        List<Role> roles = roleRepository.findAll();
        user.setRoles(roles.stream().filter(role -> role.getRole_name().equals(AppConstants.ROLE_PREFIX+AppConstants.ROLE_USER_NAME)).toList());
        user.setF_name(userRegisterDTO.getF_name());
        user.setL_name(userRegisterDTO.getL_name());
        user.setUsername(userRegisterDTO.getUsername());
        user.setEmail(userRegisterDTO.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(userRegisterDTO.getPassword()));
        user.setJoined_at(dateFormat.format(date));
        user.setUpdated_at(dateFormat.format(date));
        User createdUser = userRepository.save(user);

        return createdUser.convertDTO(createdUser);
    }

    @Override
    public UserDTO updateUser(UserUpdateDTO updateDTO) {
        User existingUser = userRepository.findFirstByUsername(updateDTO.getUsername());
        if (existingUser == null) {
            return null;
        }
        Gender gender = updateDTO.getGender();
        String firstName = updateDTO.getF_name();
        String lastName = updateDTO.getL_name();

        if (firstName != null && !firstName.equals(existingUser.getF_name())) {
            existingUser.setF_name(firstName);
        }
        if (lastName != null && !lastName.equals(existingUser.getL_name())) {
            existingUser.setL_name(lastName);
        }
        if (gender != null && !gender.equals(existingUser.getGender())){
            existingUser.setGender(gender);
        }

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);
        existingUser.setUpdated_at(dateFormat.format(date));
        userRepository.save(existingUser);

        return existingUser.convertDTO(existingUser);
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        return new User().convertDTO(userRepository.findFirstByUsername(username));
    }

    @Override
    public boolean isExistByEmail(String email) {
        return userRepository.findFirstByEmail(email) != null;
    }

    @Override
    public boolean isExistByUsername(String username) {
        return userRepository.findFirstByUsername(username) != null;
    }


    @Override
    public Page<WorkSpace> getAllWorkspace(String username, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return workspaceRepository.getAllWorkspace(username, pageable);
    }

    @Override
    public User getUserForAuthor(String username) {
        return userRepository.findFirstByUsername(username);
    }

    @Override
    public void addTokenToBlacklist(String username, String token) {
        Blacklist blacklist = new Blacklist();
        blacklist.setToken(token);
        blacklist.setUser(userRepository.findFirstByUsername(username));
        blacklistRepository.save(blacklist);
    }

    @Override
    public Blacklist checkTokenInBlacklist(String username, String token) {
        return blacklistRepository.isExistInBlacklist(getUserByUsername(username).getId(), token);
    }

    @Override
    public UserDTO updatePassword(UserUpdateDTO updateDTO) {
        User existingUser = userRepository.findFirstByUsername(updateDTO.getUsername());
        if (existingUser == null) {
            return null;
        }

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);
        existingUser.setUpdated_at(dateFormat.format(date));
        String password = updateDTO.getPassword();

        if (password != null) {
            existingUser.setPassword(new BCryptPasswordEncoder().encode(password));
        }
        userRepository.save(existingUser);

        return existingUser.convertDTO(existingUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findFirstByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("User not found",null);
        }
        return new MyUserDetails(user);
    }
}
