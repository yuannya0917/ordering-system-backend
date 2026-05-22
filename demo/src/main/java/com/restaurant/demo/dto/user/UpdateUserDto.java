package com.restaurant.demo.dto.user;

import lombok.Data;

@Data
public class UpdateUserDto {
    private String userId;
    private String username;
    private String securityQuestion;
    private String securityAnswer;
    private String merchantName;
}