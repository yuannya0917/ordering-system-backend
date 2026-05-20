package com.restaurant.demo.dto.user;

import lombok.Data;

@Data
public class RegisterDto {
    private String userId;           // 11位手机号
    private String userPassword;     // 密码
    private String userType;         // "customer" 或 "admin"
    private String securityQuestion; // 安全问题（顾客）
    private String securityAnswer;   // 安全答案（顾客）
    private String merchantName;     // 商家名称（商家）
}