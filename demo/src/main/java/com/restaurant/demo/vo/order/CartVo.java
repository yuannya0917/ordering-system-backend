// CartVo.java
package com.restaurant.demo.vo.order;

import java.util.List;

import lombok.Data;

@Data
public class CartVo {
    private List<CartItemVo> items;
    private Integer totalPrice;
}