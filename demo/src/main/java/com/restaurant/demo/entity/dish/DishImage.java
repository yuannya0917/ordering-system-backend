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
    
    private String dish_id;      // 改成 dish_id，不是 dishId
    private String dish_name;    // 改成 dish_name，不是 dishName
    private String image_url;    // 改成 image_url，不是 imageUrl
    private LocalDateTime create_time;
    private LocalDateTime update_time;
}