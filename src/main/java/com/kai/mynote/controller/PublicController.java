package com.kai.mynote.controller;

import com.kai.mynote.assets.AppConstants;
import com.kai.mynote.dto.ResponseObject;
import com.kai.mynote.dto.UserDTO;
import com.kai.mynote.dto.UserRegisterDTO;
import com.kai.mynote.dto.UserUpdateDTO;
import com.kai.mynote.entities.User;
import com.kai.mynote.service.Impl.UserServiceImpl;
import com.kai.mynote.util.JwtUtil;
import jakarta.websocket.server.PathParam;
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
@RequestMapping("/public")
public class PublicController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/sign-up")
    public ResponseEntity<ResponseObject> signUp(@RequestBody UserRegisterDTO userRegisterDTO) {
        if (userService.isExistByEmail(userRegisterDTO.getEmail())) {
            return createErrorResponse(AppConstants.EMAIL_TAKEN_WARN);
        }

        if (userService.isExistByUsername(userRegisterDTO.getUsername())) {
            return createErrorResponse(AppConstants.USERNAME_TAKEN_WARN);
        }

        UserDTO createdUser = userService.createUser(userRegisterDTO);
        if (createdUser == null) {
            return createErrorResponse(AppConstants.REGISTER_FAIL_WARN);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject(AppConstants.SUCCESS_STATUS, AppConstants.REGISTER_SUCCESS_WARN, createdUser));
    }

    private ResponseEntity<ResponseObject> createErrorResponse(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(AppConstants.FAILURE_STATUS, message, null));
    }


    @PostMapping("/sign-in")
    public ResponseEntity<ResponseObject> signIn(@RequestBody UserRegisterDTO userRegisterDTO) throws IOException {
        try {
            authenticateUser(userRegisterDTO.getUsername(), userRegisterDTO.getPassword());
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(AppConstants.FAILURE_STATUS, AppConstants.LOGIN_FAIL_WARN, null));
        }

        final UserDetails userDetails = userService.loadUserByUsername(userRegisterDTO.getUsername());


        return ResponseEntity.ok(new ResponseObject(AppConstants.SUCCESS_STATUS, AppConstants.LOGIN_SUCCESS_WARN, jwtUtil.generateToken(userDetails.getUsername())));
    }

    private void authenticateUser(String username, String password) throws AuthenticationException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

}
