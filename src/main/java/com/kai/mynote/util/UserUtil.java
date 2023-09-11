package com.kai.mynote.util;

import com.kai.mynote.repository.UserRepository;
import com.kai.mynote.service.Impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class UserUtil {

    @Autowired
    private UserRepository userRepository;

        public String generateRandomString() {
            int length = AppConstants.CODE_LENGTH;
            String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            StringBuilder randomString = new StringBuilder();

            Random random = new Random();
            for (int i = 0; i < length; i++) {
                int index = random.nextInt(characters.length());
                char randomChar = characters.charAt(index);
                randomString.append(randomChar);
            }

            return randomString.toString();
        }
    public boolean isUserActive(Authentication authentication){
        return userRepository.findFirstByUsername(authentication.getName()).isActive();
    }

}
