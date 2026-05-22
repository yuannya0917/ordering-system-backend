package com.restaurant.demo.dto.user;

import lombok.Data;

@Data
public class ForgotPasswordDto {
    private String userId;
    private String securityAnswer;
    private String newPassword;
}