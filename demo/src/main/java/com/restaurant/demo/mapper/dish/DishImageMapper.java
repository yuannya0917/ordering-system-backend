package com.restaurant.demo.mapper.dish;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.restaurant.demo.entity.dish.DishImage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Delete;

@Mapper
public interface DishImageMapper extends BaseMapper<DishImage> {
    
    // 根据菜品ID查询图片（一菜一图）
    @Select("SELECT * FROM dish_image WHERE dish_id = #{dishId} LIMIT 1")
    DishImage selectByDishId(String dishId);
    
    // 根据菜品ID删除图片
    @Delete("DELETE FROM dish_image WHERE dish_id = #{dishId}")
    int deleteByDishId(String dishId);
}