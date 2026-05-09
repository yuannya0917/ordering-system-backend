// CartItemVo.java
package com.restaurant.demo.vo.order;

import lombok.Data;

@Data
public class CartItemVo {
    private String dishId;
    private String dishName;
    private Integer dishPrice;
    private Integer dishNum;
    private Integer totalPrice;
}