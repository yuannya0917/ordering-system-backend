// AddDishDto.java
package com.restaurant.demo.dto.dish;

import lombok.Data;

@Data
public class AddDishDto {
    private String dishName;
    private Integer dishPrice;
    private String dishIntroduction;
    private String menuId;
}