package com.restaurant.demo.dto.user;

import lombok.Data;

@Data
public class DeleteAccountDto {
    private String userId;           // 要注销的账号
    private String currentUserId;    // 当前登录的用户ID
}