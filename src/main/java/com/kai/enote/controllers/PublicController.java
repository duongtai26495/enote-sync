package com.kai.enote.controllers;

import com.kai.enote.models.User;
import com.kai.enote.models.UserDTO;
import com.kai.enote.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authentication/")
@CrossOrigin("*")
public class PublicController {

    @Autowired
    private UserServiceImpl userService;

    @PostMapping("register")
    public UserDTO register(@RequestBody User user){
        UserDTO userDTO = new UserDTO();
        User savedUser = userService.saveNewUser(user);
        userDTO.setUsername(savedUser.getUsername());
        userDTO.setFullname(savedUser.getFullname());
        return userDTO;
    }

}
