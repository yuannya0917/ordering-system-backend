package com.restaurant.demo.vo.user;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class UserListVo {
    private String userId;
    private String username;
    private String securityQuestion;
    private BigDecimal totalAmount;
    private Integer orderCount;
}