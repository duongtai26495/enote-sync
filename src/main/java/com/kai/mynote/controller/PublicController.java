package com.kai.mynote.controller;

import com.kai.mynote.dto.*;
import com.kai.mynote.entities.ActivateCode;
import com.kai.mynote.entities.CodeTye;
import com.kai.mynote.entities.User;
import com.kai.mynote.service.Impl.ActivateCodeServiceImpl;
import com.kai.mynote.util.AppConstants;
import com.kai.mynote.service.Impl.FileServiceImpl;
import com.kai.mynote.service.Impl.UserServiceImpl;
import com.kai.mynote.util.JwtUtil;
import jakarta.mail.MessagingException;
import jakarta.websocket.server.PathParam;
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
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

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

    @Autowired
    private ActivateCodeServiceImpl codeService;

    private static final Logger logger = LogManager.getLogger(PublicController.class);

    @PostMapping("/sign-up")
    public ResponseEntity<ResponseObject> signUp(@RequestBody UserRegisterDTO userRegisterDTO) throws MessagingException {
        if (userService.isExistByEmail(userRegisterDTO.getEmail())) {
            return createErrorResponse(AppConstants.EMAIL_TAKEN_WARN);
        }

        if (userService.isExistByUsername(userRegisterDTO.getUsername())) {
            return createErrorResponse(AppConstants.USERNAME_TAKEN_WARN);
        }

        User createdUser = userService.createUser(userRegisterDTO);
        if (createdUser == null) {
            return createErrorResponse(AppConstants.REGISTER_FAIL_WARN);
        }
        logger.info("User created: "+createdUser.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject(AppConstants.SUCCESS_STATUS, AppConstants.REGISTER_SUCCESS_WARN, createdUser.convertDTO(createdUser)));
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
    public ResponseEntity<ResponseObject> recovery(@PathVariable String email) {
            User user = userService.getUserByEmail(email);
            if (user != null && user.isEnabled() &&
                    user.getSendRecoveryPwCount() < 3) {
                userService.sendRecoveryPwMail(userService.getUserByEmail(email));
                return ResponseEntity.ok(new ResponseObject(AppConstants.SUCCESS_STATUS, AppConstants.EMAIL_SENT, null));
            }
            if (user != null && user.getSendActivateMailCount() >= 3) {
                Date currentDate = new Date();
                Calendar currentTime = Calendar.getInstance();
                currentTime.setTime(currentDate);

                Date current = currentTime.getTime();

                Date lastTime = user.getLastSendActivateEmail();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(lastTime);
                calendar.add(Calendar.HOUR, 24);
                Date lastSent = calendar.getTime();

                if (lastSent.compareTo(current) < 0) {
                    user.setSendActivateMailCount(0);
                    userService.updateUser(user);
                    userService.sendRecoveryPwMail(userService.getUserByEmail(email));
                    logger.info("Recovery email: " + email);
                    return ResponseEntity.ok(new ResponseObject(AppConstants.SUCCESS_STATUS, AppConstants.EMAIL_SENT, null));
                }
            }
            return ResponseEntity.ok(new ResponseObject(AppConstants.FAILURE_STATUS, AppConstants.EMAIL_NOT_EXIST, null));
    }

    @GetMapping("/send-activate-mail")
    public ResponseEntity<ResponseObject> resendActiveMail(@PathParam("email") String email){
        User user = userService.getUserByEmail(email);
        if(user != null && !user.isEnabled() &&
                user.getSendActivateMailCount() < 3) {
            userService.sendActiveMail(userService.getUserByEmail(email));
            return ResponseEntity.ok(new ResponseObject(AppConstants.SUCCESS_STATUS, AppConstants.EMAIL_SENT, null));
        }
        if(user != null && user.getSendActivateMailCount() >= 3 ) {
            Date currentDate = new Date();
            Calendar currentTime = Calendar.getInstance();
            currentTime.setTime(currentDate);

            Date current = currentTime.getTime();

            Date lastTime = user.getLastSendActivateEmail();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(lastTime);
            calendar.add(Calendar.HOUR,24);
            Date lastSent = calendar.getTime();

            if (lastSent.compareTo(current) < 0) {
                user.setSendActivateMailCount(0);
                userService.updateUser(user);
                userService.sendActiveMail(userService.getUserByEmail(email));
                logger.info("Resend active email: " + email);
                return ResponseEntity.ok(new ResponseObject(AppConstants.SUCCESS_STATUS, AppConstants.EMAIL_SENT, null));
            }
            return ResponseEntity.ok(new ResponseObject(AppConstants.FAILURE_STATUS, AppConstants.LIMIT_SEND_EMAIL,null));
        }
        return ResponseEntity.ok(new ResponseObject(AppConstants.FAILURE_STATUS, AppConstants.ACCOUNT_ACTIVATED,null));
    }

    @PostMapping("/activate-account")
    public ResponseEntity<ResponseObject> activateAccount(@RequestBody ActivateCode activateCode) throws ParseException {
        User user = userService.getUserByEmail(activateCode.getEmail());
        ActivateCode code = codeService.findCodeByCode(activateCode.getCode());
        if (user != null){
            if(!code.isUsed() && code.getType().equals(CodeTye.ACTIVE)
            && user.getEmail().equalsIgnoreCase(activateCode.getEmail())){
                Date currentDate = new Date();
                Calendar currentTime = Calendar.getInstance();
                currentTime.setTime(currentDate);
                Date current = currentTime.getTime();

                if(current.compareTo(code.getExpiredAt()) < 0 ){
                    userService.setActiveUser(code.getEmail(), true);
                    codeService.updateCode(code);
                    return ResponseEntity.ok(new ResponseObject(AppConstants.SUCCESS_STATUS, AppConstants.USER + " " + AppConstants.ACTIVATED,null));
                }
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(AppConstants.FAILURE_STATUS, AppConstants.CODE_EXPIRED, null));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(AppConstants.FAILURE_STATUS, AppConstants.ACTIVATED_FAIL, null));
    }

    @PostMapping("/recovery-password/{code}")
    public ResponseEntity<ResponseObject> recoveryPassword(@PathVariable String code, @RequestBody User user) throws ParseException {
        User currentUser = userService.getUserByEmail(user.getEmail());
        ActivateCode recoveryCode = codeService.findCodeByCode(code);
        if (currentUser != null){
            if(!recoveryCode.isUsed() && recoveryCode.getType().equals(CodeTye.RECOVERY)
                    && user.getEmail().equalsIgnoreCase(recoveryCode.getEmail())){
                Date currentDate = new Date();
                Calendar currentTime = Calendar.getInstance();
                currentTime.setTime(currentDate);
                Date current = currentTime.getTime();

                if(current.compareTo(recoveryCode.getExpiredAt()) < 0 ){
                    codeService.updateCode(recoveryCode);
                    userService.updatePassword(user);
                    return ResponseEntity.ok(new ResponseObject(AppConstants.SUCCESS_STATUS, AppConstants.PASSWORD_UPDATED,null));
                }
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(AppConstants.FAILURE_STATUS, AppConstants.CODE_EXPIRED, null));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(AppConstants.FAILURE_STATUS, AppConstants.USER_FOUND, null));
    }

}
