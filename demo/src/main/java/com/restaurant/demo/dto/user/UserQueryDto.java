package com.restaurant.demo.dto.user;

import lombok.Data;

@Data
public class UserQueryDto {
    private String userId;           // 按手机号查询（可选）
    private String username;         // 按用户名查询（可选）
    private String currentUserId;    // 当前登录用户ID
    private Integer page = 1;        // 页码
    private Integer pageSize = 10;   // 每页数量
}