package com.kai.mynote.service.Impl;

import com.kai.mynote.config.MyUserDetails;
import com.kai.mynote.dto.UserDTO;
import com.kai.mynote.dto.UserRegisterDTO;
import com.kai.mynote.entities.Role;
import com.kai.mynote.entities.User;
import com.kai.mynote.repository.RoleRepository;
import com.kai.mynote.repository.UserRepository;
import com.kai.mynote.service.UserService;
import com.sun.tools.jconsole.JConsoleContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


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
    public UserDTO updateUser(User user) {
        return null;
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findFirstByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("User not found",null);
        }
        return new MyUserDetails(user);
    }
}
