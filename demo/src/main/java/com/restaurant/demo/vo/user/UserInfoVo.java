package com.restaurant.demo.vo.user;

import lombok.Data;

@Data
public class UserInfoVo {
    private String userId;
    private String userType;
    private String securityQuestion;
    private String securityAnswer;
    private String merchantName;
}