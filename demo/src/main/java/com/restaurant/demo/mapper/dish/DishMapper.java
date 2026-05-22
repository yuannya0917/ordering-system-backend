// DishMapper.java
package com.restaurant.demo.mapper.dish;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.restaurant.demo.entity.dish.Dish;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
    
    // 原有的方法（保留）
    @Select("SELECT * FROM dish WHERE menuID = #{menuId}")
    List<Dish> selectDishListByMenuId(String menuId);
    
    // 新增的方法（支持 dishId、dishName、menuId 查询）
    @Select("<script>" +
            "SELECT d.*, m.menuName FROM dish d " +
            "LEFT JOIN menu m ON d.menuID = m.menuID " +
            "WHERE 1=1 " +
            "<if test='dishId != null and dishId != \"\"'> AND d.dishID = #{dishId} </if>" +
            "<if test='dishName != null and dishName != \"\"'> AND d.dishName LIKE CONCAT('%', #{dishName}, '%') </if>" +
            "<if test='menuId != null and menuId != \"\"'> AND d.menuID = #{menuId} </if>" +
            "</script>")
    List<Map<String, Object>> selectDishList(@Param("dishId") String dishId, 
                                              @Param("dishName") String dishName,
                                              @Param("menuId") String menuId);
}