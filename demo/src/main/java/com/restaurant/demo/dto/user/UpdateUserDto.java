package com.restaurant.demo.dto.user;

import lombok.Data;

@Data
public class UpdateUserDto {
    private String userId;           // 要修改的用户ID
    private String securityQuestion; // 新安全问题（顾客专用）
    private String securityAnswer;   // 新安全答案（顾客专用）
    private String merchantName;     // 新商家名称（商家专用）
}