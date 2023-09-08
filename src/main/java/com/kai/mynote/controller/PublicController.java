package com.kai.mynote.controller;

import com.kai.mynote.dto.*;
import com.kai.mynote.entities.User;
import com.kai.mynote.util.AppConstants;
import com.kai.mynote.service.Impl.FileServiceImpl;
import com.kai.mynote.service.Impl.UserServiceImpl;
import com.kai.mynote.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/public")
public class PublicController {
    @Value("${upload.path}")
    private String uploadPath;
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private FileServiceImpl fileService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private static final Logger logger = LogManager.getLogger(PublicController.class);

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
        logger.info("User created: "+createdUser.getUsername());
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

        String loggedInUsername = userDetails.getUsername();
        logger.info("User logged in: "+loggedInUsername);
        return ResponseEntity.ok(new ResponseObject(AppConstants.SUCCESS_STATUS, AppConstants.LOGIN_SUCCESS_WARN, jwtUtil.generateToken(loggedInUsername)));
    }

    private void authenticateUser(String username, String password) throws AuthenticationException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    @GetMapping("/image/{imageName}")
    public ResponseEntity<byte[]> displayImage(@PathVariable String imageName) {
        return fileService.getImage(imageName);
    }

    @GetMapping("/check-username/{username}")
    public boolean checkUsernameIsExist(@PathVariable String username){
        return userService.isExistByUsername(username);
    }
    @GetMapping("/check-email/{email}")
    public boolean checkEmailIsExist(@PathVariable String email){
        return userService.isExistByEmail(email);
    }

    @GetMapping("/recovery/{email}")
    public ResponseEntity<ResponseObject> recovery(@PathVariable String email){
        if(userService.isExistByEmail(email)){
            logger.info("Recovery email: "+email );
            return ResponseEntity.ok(new ResponseObject(AppConstants.SUCCESS_STATUS, AppConstants.EMAIL_SENT,null));
        }
        return ResponseEntity.ok(new ResponseObject(AppConstants.FAILURE_STATUS, AppConstants.EMAIL_NOT_EXIST,null));
    }

    @PostMapping("/activate-account")
    public ResponseEntity<ResponseObject> activateAccount(@RequestBody ActiveForm activeForm){
        User user = userService.getUserForAuthor(activeForm.getUsername());
        if (user != null){
            if(user.getActiveCode().equalsIgnoreCase(activeForm.getActive_code())){
                userService.setActiveUser(activeForm.getUsername(), true);
                return ResponseEntity.ok(new ResponseObject(AppConstants.SUCCESS_STATUS, AppConstants.USER + " " + AppConstants.ACTIVATED,null));
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(AppConstants.FAILURE_STATUS, AppConstants.ACTIVATED_FAIL, null));
    }
}
