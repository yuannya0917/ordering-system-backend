package com.restaurant.demo.dto.order;

import lombok.Data;

@Data
public class AddToCartDto {
    private String dishId;
    private String dishName;
    private Integer dishPrice;
    private Integer dishNum;
}