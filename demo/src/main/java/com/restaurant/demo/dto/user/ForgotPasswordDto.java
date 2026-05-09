package com.restaurant.demo.dto.user;

import lombok.Data;

@Data
public class ForgotPasswordDto {
    private String userId;           // 账号
    private String securityAnswer;   // 密保答案
    private String newPassword;      // 新密码
}