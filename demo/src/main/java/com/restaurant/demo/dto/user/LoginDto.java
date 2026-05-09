package com.restaurant.demo.dto.user;

import lombok.Data;

@Data
public class LoginDto {
    private String userId;
    private String userPassword;
}