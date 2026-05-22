package com.restaurant.demo.vo.dish;

import lombok.Data;

@Data
public class DishVo {
    private String dishId;           // 菜品ID（保持不变）
    private String dishName;
    private Integer dishPrice;
    private String dishIntroduction;
    private String menuName;         // 所属菜单名称
    private String dishImage;        // 菜品图片URL（从 dish_image 表获取）
}