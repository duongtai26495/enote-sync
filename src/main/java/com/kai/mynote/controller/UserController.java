package com.kai.mynote.controller;


import com.kai.mynote.dto.ResponseObject;
import com.kai.mynote.dto.UserDTO;
import com.kai.mynote.dto.UserUpdateDTO;
import com.kai.mynote.entities.User;
import com.kai.mynote.service.Impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("{username}")
    public ResponseEntity<ResponseObject> getInfoUser(@PathVariable String username, Authentication authentication){
        if ( authentication.getName().equalsIgnoreCase(username)){
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("SUCCESS","User information", userService.getUserByUsername(username)));
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject("FAILED","Get user information failed", null));
        }
    }

    @PutMapping("update")
    public ResponseEntity<ResponseObject> updateUser(@RequestBody UserUpdateDTO updateDTO, Authentication authentication){
        if ( authentication.getName().equalsIgnoreCase(updateDTO.getUsername())){
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("SUCCESS","User updated", userService.updateUser(updateDTO)));
        };
        return createErrorResponse("Not permission");
    }


    private ResponseEntity<ResponseObject> createErrorResponse(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("FAILED", message, null));
    }

    private ResponseEntity<ResponseObject> createSuccessResponse(UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("SUCCESS", "User Registered Successfully", userDTO));
    }
}
