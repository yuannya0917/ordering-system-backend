package com.restaurant.demo.entity.user;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("customer")
public class Customer {
    @TableId
    private String userId;
    private String securityQuestion;
    private String securityAnswer;
}