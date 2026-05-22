// OrderVo.java
package com.restaurant.demo.vo.order;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class OrderVo {
    private String orderId;
    private String userId;
    private Integer orderPrice;
    private LocalDateTime orderTime;
    private String orderNote;
    private String orderStatus;
}