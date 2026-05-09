// DishMapper.java
package com.restaurant.demo.mapper.dish;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.restaurant.demo.entity.dish.Dish;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
    
    @Select("SELECT * FROM dish WHERE menu_id = #{menuId} AND deleted = 0")
    List<Dish> selectDishListByMenuId(String menuId);
}