package com.kai.mynote.controller;

import com.kai.mynote.dto.ResponseObject;
import com.kai.mynote.dto.UserDTO;
import com.kai.mynote.dto.UserRegisterDTO;
import com.kai.mynote.dto.UserUpdateDTO;
import com.kai.mynote.service.Impl.UserServiceImpl;
import com.kai.mynote.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/public/")
public class PublicController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("hello")
    public String hello(){
        return "Hello";
    }

    @PostMapping("sign-up")
    public ResponseEntity<ResponseObject> signUp(@RequestBody UserRegisterDTO userRegisterDTO) {
        if (userService.isExistByEmail(userRegisterDTO.getEmail())) {
            return createErrorResponse("This email already taken");
        }

        if (userService.isExistByUsername(userRegisterDTO.getUsername())) {
            return createErrorResponse("This username already taken");
        }

        UserDTO createdUser = userService.createUser(userRegisterDTO);
        if (createdUser == null) {
            return createErrorResponse("User Register Fail");
        }

        return createSuccessResponse(createdUser);
    }

    private ResponseEntity<ResponseObject> createErrorResponse(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("FAILED", message, null));
    }

    private ResponseEntity<ResponseObject> createSuccessResponse(UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("SUCCESS", "User Registered Successfully", userDTO));
    }

    @PostMapping("sign-in")
    public ResponseEntity<ResponseObject> signIn(@RequestBody UserRegisterDTO userRegisterDTO) throws IOException {
        try {
            authenticateUser(userRegisterDTO.getUsername(), userRegisterDTO.getPassword());
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("FAILED", "User Login Fail", null));
        }

        final UserDetails userDetails = userService.loadUserByUsername(userRegisterDTO.getUsername());

//        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(new ResponseObject("SUCCESS", "User Login Successfully", jwtUtil.generateToken(userDetails.getUsername())));
    }

    private void authenticateUser(String username, String password) throws AuthenticationException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }


}
