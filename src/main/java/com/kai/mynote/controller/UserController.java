package com.kai.mynote.controller;


import com.kai.mynote.entities.Note;
import com.kai.mynote.entities.User;
import com.kai.mynote.service.Impl.FileServiceImpl;
import com.kai.mynote.service.Impl.NoteServiceImpl;
import com.kai.mynote.util.AppConstants;
import com.kai.mynote.dto.ResponseObject;
import com.kai.mynote.dto.UserDTO;
import com.kai.mynote.dto.UserUpdateDTO;
import com.kai.mynote.service.Impl.UserServiceImpl;
import com.kai.mynote.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private FileServiceImpl fileService;

    @Autowired
    private UserServiceImpl userService;


    @Autowired
    private JwtUtil jwtUtil;

    private static final Logger logger = LogManager.getLogger(UserController.class);

    @GetMapping("info/{username}")
    public ResponseEntity<ResponseObject> getInfoUser(@PathVariable String username, Authentication authentication){
        if ( authentication.getName().equalsIgnoreCase(username)){
            logger.info("Get infor user: "+username);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(AppConstants.SUCCESS_STATUS,"User information", userService.getUserByUsername(username)));
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(AppConstants.FAILURE_STATUS,"Get user information failed", null));
        }
    }

    @PutMapping("update")
    public ResponseEntity<ResponseObject> updateUser(@RequestBody UserUpdateDTO updateDTO, Authentication authentication){
        if ( authentication.getName().equalsIgnoreCase(updateDTO.getUsername())){
            logger.info("User updated: "+updateDTO.getUsername());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(AppConstants.SUCCESS_STATUS,"User updated", userService.updateUser(updateDTO)));
        };
        return createErrorResponse();
    }


    private ResponseEntity<ResponseObject> createErrorResponse() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(AppConstants.FAILURE_STATUS, "Not permission", null));
    }


    @GetMapping("refresh")
    public ResponseEntity<ResponseObject> refreshToken(Authentication authentication, HttpServletRequest request){
        if (getAndAddTokenToBlackList(authentication, request)) {
            logger.info("User refresh token: "+authentication.getName());
            return ResponseEntity.ok(new ResponseObject(AppConstants.SUCCESS_STATUS, "Refresh token Successfully", jwtUtil.generateToken(authentication.getName())));
        }
        return createErrorResponse();
    }

    @PutMapping("update-password")
    public ResponseEntity<ResponseObject> updatePassword(@RequestBody UserUpdateDTO updateDTO, Authentication authentication, HttpServletRequest request){

        if ( authentication.getName().equalsIgnoreCase(updateDTO.getUsername())){
            if (getAndAddTokenToBlackList(authentication, request)) {
                logger.info("User update password: "+authentication.getName());
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(AppConstants.SUCCESS_STATUS, "Password updated", userService.updatePassword(updateDTO)));
            }
        };

        return createErrorResponse();
    }

    @PostMapping("upload/image/{username}")
    private ResponseEntity<ResponseObject> uploadProfileImage(@PathVariable String username,
                                                              Authentication authentication,
                                                              @RequestParam("user_profile_image") MultipartFile file){
        if(authentication.getName().equalsIgnoreCase(username)) {
            try {
                // Kiểm tra kích thước tệp ảnh
                long fileSize = file.getSize();
                if (fileSize > AppConstants.MAX_FILE_SIZE) {
                    logger.warn("User "+username+" uploaded large image: "+file.getSize());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                            new ResponseObject(AppConstants.FAILURE_STATUS, AppConstants.BAD_REQUEST_MSG, null)
                    );
                }

                // Kiểm tra xem tệp có phải là ảnh không
                if (!fileService.isImage(file)) {
                    logger.warn("User "+username+" uploaded not an image file");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                            new ResponseObject(AppConstants.FAILURE_STATUS, AppConstants.BAD_REQUEST_MSG, null)
                    );
                }

                String imageURL = fileService.storeNoteImage(file);
                UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
                userUpdateDTO.setProfile_image(imageURL);
                userUpdateDTO.setUsername(username);

                userService.updateUser(userUpdateDTO);
                logger.info("User "+username+" uploaded an profile image");
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject(AppConstants.SUCCESS_STATUS, AppConstants.USER + " " + AppConstants.UPDATED, imageURL)
                );
            } catch (IOException e) {
                logger.error("Upload image error: "+e);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ResponseObject(AppConstants.FAILURE_STATUS, AppConstants.BAD_REQUEST_MSG, null)
                );
            }
        }

        logger.error("Upload an image by not authorized: "+username);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ResponseObject(AppConstants.FAILURE_STATUS, AppConstants.NOT_PERMISSION, null)
        );
    }

    @GetMapping("analytics")
    private ResponseEntity<ResponseObject> getUserAnalytics(Authentication authentication){
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(AppConstants.SUCCESS_STATUS, AppConstants.USER, userService.userAnalytics(authentication.getName()))
        );
    }




    private boolean getAndAddTokenToBlackList (Authentication authentication, HttpServletRequest request){
        String authHeader = request.getHeader(AppConstants.AUTH_HEADER);
        String username = authentication.getName();

        if (authHeader != null && authHeader.startsWith(AppConstants.BEARER_TOKEN_PREFIX)) {
            String token = authHeader.substring(7);
            if (jwtUtil.validateToken(token, userService.loadUserByUsername(username))){
                userService.addTokenToBlacklist(username, token);
                logger.info("Added token to blacklist: "+username);
                return true;
            }
        }
        return false;
    }


}
