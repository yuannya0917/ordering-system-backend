package com.restaurant.demo.dto.user;

import lombok.Data;

@Data
public class UpdatePasswordDto {
    private String userId;
    private String verifyType;        // "password" 或 "security"
    private String oldPassword;       // 密码验证时使用
    private String securityAnswer;    // 密保验证时使用
    private String newPassword;
}