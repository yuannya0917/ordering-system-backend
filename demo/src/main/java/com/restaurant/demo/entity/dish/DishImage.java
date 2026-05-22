package com.restaurant.demo.entity.dish;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("dish_image")
public class DishImage {
    @TableId
    private Integer id;
    private String dishId;
    private String dishName;
    private String imageUrl;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}