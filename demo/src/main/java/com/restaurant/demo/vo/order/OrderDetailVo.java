// OrderDetailVo.java
package com.restaurant.demo.vo.order;

import lombok.Data;

@Data
public class OrderDetailVo {
    private String orderId;
    private String dishId;
    private String dishName;
    private Integer dishNum;
    private Integer dishPrice;
    private Integer totalPrice;
}