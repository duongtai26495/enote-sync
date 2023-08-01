package com.kai.mynote.service.Impl;

import com.kai.mynote.config.MyUserDetails;
import com.kai.mynote.dto.UserDTO;
import com.kai.mynote.dto.UserRegisterDTO;
import com.kai.mynote.dto.UserUpdateDTO;
import com.kai.mynote.entities.Note;
import com.kai.mynote.entities.Role;
import com.kai.mynote.entities.User;
import com.kai.mynote.entities.WorkSpace;
import com.kai.mynote.repository.NoteRepository;
import com.kai.mynote.repository.RoleRepository;
import com.kai.mynote.repository.UserRepository;
import com.kai.mynote.repository.WorkspaceRepository;
import com.kai.mynote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


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

    @Override
    public UserDTO createUser(UserRegisterDTO userRegisterDTO) {
        User user = new User();
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findRoleByName("ROLE_USER"));
        user.setRoles(roles);
        user.setF_name(userRegisterDTO.getF_name());
        user.setL_name(userRegisterDTO.getL_name());
        user.setUsername(userRegisterDTO.getUsername());
        user.setEmail(userRegisterDTO.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(userRegisterDTO.getPassword()));
        User createdUser = userRepository.save(user);

        return createdUser.convertDTO(createdUser);
    }

    @Override
    public User updateUser(UserUpdateDTO updateDTO) {
        User existingUser = userRepository.findFirstByUsername(updateDTO.getUsername());
        if (existingUser == null) {
            return null;
        }

        String firstName = updateDTO.getF_name();
        String lastName = updateDTO.getL_name();
        String password = updateDTO.getPassword();

        if (firstName != null) {
            existingUser.setF_name(firstName);
        }
        if (lastName != null) {
            existingUser.setL_name(lastName);
        }
        if (password != null) {
            existingUser.setPassword(password);
        }

        return userRepository.save(existingUser);
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findFirstByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("User not found",null);
        }
        return new MyUserDetails(user);
    }
}
