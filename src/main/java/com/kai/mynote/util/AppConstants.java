package com.kai.mynote.util;

public final class AppConstants {
    public static final int ACCESS_TOKEN_EXPIRATION_MS = 24 * 60 * 60 * 1000; // 24 giờ
    public static final long REFRESH_TOKEN_EXPIRATION_MS = 15 * 24 * 60 * 60 * 1000; // 15 ngày
    public static final String SECRET_KEY = "5367566B597033733627639792F423F45281482B4D6251655468576D5A71347437";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String ACCOUNT_ACTIVATED = "account already activated";
    public static final String LIMIT_SEND_EMAIL = "This account is achieved limit, try again at tomorrow";
    public static final String ROLE_ADMIN_NAME = "ADMIN";
    public static final String ROLE_USER_NAME = "USER";
    public static final String SUCCESS_STATUS = "SUCCESS";
    public static final String FAILURE_STATUS = "FAILURE";
    public static final String BAD_REQUEST_MSG = "Bad request";
    public static final String EMAIL_SENT = "Email sent";
    public static final String CODE_EXPIRED = "Code is expired";
    public static final String CODE_INCORRECT = "Code is incorrect";
    public static final String EMAIL_NOT_EXIST = "Email do not exist";
    public static final String USERNAME_PASSWORD_WRONG = "Username or Password is incorrect";
    public static final String NOTE = "note";
    public static final String USER = "user";
    public static final String WORKSPACE = "workspace";
    public static final String TASK = "task";
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
    public static final String ACTIVATED_FAIL = "Activated fail";
    public static final String API_PREFIX_V1 = "/api/v1/";

    public static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    public static final String FOUNDED = "FOUNDED";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String USER_FOUND = "User found";
    public static final String YOU_DONT_HAVE_PERMISSION = "You don't have permission!";
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";
    public static final String EMAIL_ALREADY_TAKEN = "This email already taken!";
    public static final String USERNAME_ALREADY_TAKEN = "This username already taken!";
    public static final String USER_LOGGED_IN = "User logged in";
    public static final String USER_DO_NOT_LOGIN = "User do not login";
    public static final String USER_CREATE_SUCCESSFULLY = "User create successfully";
    public static final String USER_EDITED = "User edited";
    public static final String PASSWORD_UPDATED = "Your password updated";
    public static final String UPLOAD_PROFILE_IMAGE_SUCCESS = "Upload profile image success";
    public static final String UPLOAD_IMAGE_SUCCESS = "Upload image success";
    public static final String NOT_PERMISSION = "You do not permission";
    public static final String HAVE_ERROR = "Have an error. Try again";
    public static final String NOT_FOUND = "Not found";
    public static final String REMOVE_MEDIA = "Remove media";

    //role
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_EXIST = "Create role %s failed, this role already exist!";
    public static final String CREATE_ROLE_SUCCESS = "The role %s create successfully";

    //time
    public static final String TIME_PATTERN = "dd/MM/yy hh:mm:ss aa";

    //file
    public static final String CANNOT_INITIALIZE_STORAGE = "Cannot initialize storage";
    public static final String FAILED_STORE_EMPTY_FILE = "Failed to store empty file";
    public static final String YOU_CAN_ONLY_UPLOAD_IMAGE = "You can only upload image file";
    public static final String SIZE_UPLOAD_FILE = "Image must be <= 5mb";
    public static final String CANNOT_STORE_OUTSIDE = "Cannot store file outside current directory";
    public static final String STORE_FILE_FAILED = "Failed to store the file";

    //diary
    public static final String DIARY_NOT_FOUND = "Diary not found";
    public static final String DIARY_DELETED = "Diary deleted";
    public static final String DIARY_EDITED = "Diary edited";
    public static final String LIST_DIARY_BY = "List diary by %s";
    public static final String UNTITLED_DIARY = "Untitled Diary";
    public static final String DIARY_CREATE_SUCCESS = "Diary create successfully";
    public static final String DIARY_FOUND = "Diary found";

    public static final String ACTIVATED = "activated";

    //token
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final long EXPIRATION_TIME = 900_000;

    //authen
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String SECRET_CODE = "secret";
    public static final String ROLES = "roles";

    //sort
    public static final String LAST_EDITED_DESC_LABEL = "Last Edited - Newest";
    public static final String LAST_EDITED_ASC_LABEL = "Last Edited - Oldest";
    public static final String CREATED_AT_DESC_LABEL = "Created At - Newest";
    public static final String CREATED_AT_ASC_LABEL = "Created At - Oldest";
    public static final String A_Z_LABEL = "Alphabet A -> Z";
    public static final String Z_A_LABEL = "Alphabet Z -> A";
    public static final String DONE_LAST_UPDATED_ASC_LABEL = "Finish by last updated, old to new";
    public static final String DONE_LAST_UPDATED_DESC_LABEL = "Finish by last updated, new to old";
    public static final String FAVORITE_DESC_LABEL = "My favorites, New to Old";
    public static final String FAVORITE_ASC_LABEL = "My favorites, Old to New";

    public static final String LAST_EDITED_DESC_VALUE = "updated_at_desc";
    public static final String LAST_EDITED_ASC_VALUE = "updated_at_asc";
    public static final String CREATED_AT_DESC_VALUE = "created_at_desc";
    public static final String CREATED_AT_ASC_VALUE = "created_at_asc";
    public static final String A_Z_VALUE = "a_z";
    public static final String Z_A_VALUE = "z_a";
    public static final String DONE_LAST_UPDATED_ASC_VALUE = "done_last_updated_asc";

    public static final String FAVORITE_DESC_VALUE = "favorite_desc";
    public static final String FAVORITE_ASC_VALUE = "favorite_asc";

    public static final String DONE_LAST_UPDATED_DESC_VALUE = "done_last_updated_desc";
    public static final String SUBJECT_ACTIVE_CONTENT = "Activate your account";
    public static final String SUBJECT_RECOVERY_CONTENT = "Recovery password";
    public static final String ACTIVE_EMAIL_CONTENT = """
            <html>
            <head>
                <title>Activate Your Account</title>
            </head>
            <body>
                <h3>Welcome to our application!</h3>
                <p>Here is your activation code:</p>
                <p style="font-size: 24px; font-weight: bold; color: #007bff;">%s</p>
                <p>Please use this code to activate your account.</p>
                <p style="font-weight:bold;color:red;">This code will expired after 5 minutes.</p>
                <p>If you did not request this, please ignore this email.</p>
                <p>Thank you!</p>
            </body>
            </html>
            """;
    public static final String RECOVERY_PASSWORD_EMAIL_CONTENT = """
            <html lang="en">
                   <head>
                       <meta charset="UTF-8">
                       <meta name="viewport" content="width=device-width, initial-scale=1.0">
                       <title>Mã Code Khôi Phục Mật Khẩu</title>
                       <style>
                           /* Thêm CSS để tạo giao diện email hấp dẫn */
                           body {
                               font-family: Arial, sans-serif;
                               background-color: #f4f4f4;
                               margin: 0;
                               padding: 0;
                           }
                           .container {
                               max-width: 600px;
                               margin: 0 auto;
                               padding: 20px;
                               background-color: #ffffff;
                               border-radius: 5px;
                               box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                           }
                           h2 {
                               color: #333;
                           }
                           p {
                               color: #666;
                           }
                           .code {
                               font-size: 24px;
                               font-weight: bold;
                               text-align: center;
                               margin: 20px 0;
                           }
                       </style>
                   </head>
                   <body>
                       <div class="container">
                           <h2>Mã Code Khôi Phục Mật Khẩu</h2>
                           <p>Xin chào,</p>
                           <p>Dưới đây là mã code để khôi phục mật khẩu của bạn:</p>
                           <div class="code">%s</div>
                           <p style="color:red;font-weight:bold;">Hãy sử dụng mã code này trên trang đặt lại mật khẩu của bạn. Mã này sẽ hết hạn sau 15 phút.</p>
                           <p>Nếu bạn không thực hiện yêu cầu này, bạn có thể bỏ qua email này.</p>
                           <p>Cảm ơn bạn!</p>
                       </div>
                   </body>
                   </html>
            """;
    public static final int CODE_LENGTH = 10;
}
