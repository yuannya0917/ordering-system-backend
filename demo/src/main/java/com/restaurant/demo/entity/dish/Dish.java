// Dish.java
package com.restaurant.demo.entity.dish;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("dish")
public class Dish {
    
    @TableId(type = IdType.INPUT)
    @TableField("dishID")
    private String dishId;
    
    private String dishName;
    
    private Integer dishPrice;
    
    private String dishIntroduction;
    
    @TableField("menuID")
    private String menuId;
}