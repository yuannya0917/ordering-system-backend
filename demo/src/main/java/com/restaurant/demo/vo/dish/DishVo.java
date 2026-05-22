// DishVo.java
package com.restaurant.demo.vo.dish;

import lombok.Data;

@Data
public class DishVo {
    private String dishId;
    private String dishImage;
    private String dishName;
    private Integer dishPrice;
    private String dishIntroduction;
    private String menuId;
    private String menuName;
}