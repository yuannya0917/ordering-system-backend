// UpdateDishDto.java
package com.restaurant.demo.dto.dish;

import lombok.Data;

@Data
public class UpdateDishDto {
    private String dishId;
    private String dishName;
    private Integer dishPrice;
    private String dishIntroduction;
    private String menuId;
}