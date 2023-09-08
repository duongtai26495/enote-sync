package com.kai.mynote.dto;


import lombok.Data;

@Data
public class TokenObject {
    private String access_token;
    private String refresh_token;
}
