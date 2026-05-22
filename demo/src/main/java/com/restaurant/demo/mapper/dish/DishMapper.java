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
    
    @Select("SELECT * FROM dish WHERE menuID = #{menuId}")
    List<Dish> selectDishListByMenuId(String menuId);
    
    // 修改这个方法，关联 menu 表和 dish_image 表
    @Select("<script>" +
            "SELECT d.dishID, d.dishName, d.dishPrice, d.dishIntroduction, " +
            "       m.menuName, " +
            "       di.image_url as dishImage " +
            "FROM dish d " +
            "LEFT JOIN menu m ON d.menuID = m.menuID " +
            "LEFT JOIN dish_image di ON d.dishID = di.dish_id " +
            "WHERE 1=1 " +
            "<if test='dishId != null and dishId != \"\"'> AND d.dishID = #{dishId} </if>" +
            "<if test='dishName != null and dishName != \"\"'> AND d.dishName LIKE CONCAT('%', #{dishName}, '%') </if>" +
            "<if test='menuId != null and menuId != \"\"'> AND d.menuID = #{menuId} </if>" +
            "</script>")
    List<Map<String, Object>> selectDishList(@Param("dishId") String dishId, 
                                              @Param("dishName") String dishName,
                                              @Param("menuId") String menuId);
}