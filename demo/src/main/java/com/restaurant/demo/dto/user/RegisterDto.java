package com.restaurant.demo.dto.user;

import lombok.Data;

@Data
public class RegisterDto {
    private String userId;           // 手机号
    private String userPassword;     // 密码
    private String userType;         // "customer" 或 "admin"

    // Customer 专用
    private String securityQuestion;
    private String securityAnswer;

    // Admin 专用
    private String merchantName;
}