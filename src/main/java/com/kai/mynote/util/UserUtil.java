package com.kai.mynote.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class UserUtil {
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


}
