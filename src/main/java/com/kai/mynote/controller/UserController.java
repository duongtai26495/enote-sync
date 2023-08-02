package com.kai.mynote.controller;


import com.kai.mynote.dto.ResponseObject;
import com.kai.mynote.dto.UserDTO;
import com.kai.mynote.dto.UserUpdateDTO;
import com.kai.mynote.service.Impl.UserServiceImpl;
import com.kai.mynote.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("info/{username}")
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
        return createErrorResponse();
    }


    private ResponseEntity<ResponseObject> createErrorResponse() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("FAILED", "Not permission", null));
    }

    private ResponseEntity<ResponseObject> createSuccessResponse(UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("SUCCESS", "User Registered Successfully", userDTO));
    }

    @GetMapping("refresh")
    public ResponseEntity<ResponseObject> refreshToken(Authentication authentication){
        return ResponseEntity.ok(new ResponseObject("SUCCESS", "User Login Successfully", jwtUtil.generateToken(authentication.getName())));
    }

    @PutMapping("update-password")
    public ResponseEntity<ResponseObject> updatePassword(@RequestBody UserUpdateDTO updateDTO, Authentication authentication, HttpServletRequest request){

        if ( authentication.getName().equalsIgnoreCase(updateDTO.getUsername())){
            String authHeader = request.getHeader("Authorization");
            String token = null;
            String username = authentication.getName();

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                userService.addTokenToBlacklist(username, token);
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("SUCCESS","Password updated", userService.updatePassword(updateDTO)));
            }
            
        };

        return createErrorResponse();
    }
}
