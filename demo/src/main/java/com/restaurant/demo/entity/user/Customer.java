package com.restaurant.demo.entity.user;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("Customer")
public class Customer {
    @TableId
    private String userId;

    private String userPassword;

    private String securityQuestion;

    private String securityAnswer;
}