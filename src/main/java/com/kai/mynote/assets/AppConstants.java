package com.kai.mynote.assets;

public final class AppConstants {
    public static final int ACCESS_TOKEN_EXPIRATION_MS = 24 * 60 * 60 * 1000; // 24 giờ
    public static final long REFRESH_TOKEN_EXPIRATION_MS = 15 * 24 * 60 * 60 * 1000; // 15 ngày
    public static final String SECRET_KEY = "5367566B597033733627639792F423F45281482B4D6251655468576D5A71347437";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String ROLE_ADMIN_NAME = "ADMIN";
    public static final String ROLE_USER_NAME = "USER";
    public static final String SUCCESS_STATUS = "SUCCESS";
    public static final String FAILURE_STATUS = "FAILURE";
    public static final String BAD_REQUEST_MSG = "Bad request";
    public static final String NOTE = "note";
    public static final String USER = "user";
    public static final String WORKSPACE = "workspace";
    public static final String CREATED = "created";
    public static final String UPDATED = "updated";
    public static final String REMOVED = "removed";
    public static final String EMAIL_TAKEN_WARN = "This email already taken";
    public static final String USERNAME_TAKEN_WARN = "This username already taken";
    public static final String LOGIN_FAIL_WARN = "User login fail";
    public static final String LOGIN_SUCCESS_WARN = "User login success";
    public static final String REGISTER_FAIL_WARN = "User register fail";
    public static final String REGISTER_SUCCESS_WARN = "User register success";
    public static final String AUTH_HEADER = "Authorization";
    public static final String BEARER_TOKEN_PREFIX = "Bearer ";

}
