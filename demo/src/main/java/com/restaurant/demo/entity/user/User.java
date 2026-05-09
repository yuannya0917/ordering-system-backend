package com.restaurant.demo.entity.user;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("User")
public class User {
    @TableId
    private String userId;

    private String userPassword;

    private String userType;  // "customer" 或 "admin"
}