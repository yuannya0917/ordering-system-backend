// DishService.java
package com.restaurant.demo.service.dish;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.restaurant.demo.dto.dish.AddDishDto;
import com.restaurant.demo.dto.dish.UpdateDishDto;
import com.restaurant.demo.entity.dish.Dish;
import com.restaurant.demo.vo.dish.DishVo;

public interface DishService extends IService<Dish> {
    
    List<DishVo> getDishList(String menuId);
    
    DishVo getDish(String dishId);
    
    boolean addDish(AddDishDto addDishDto);
    
    boolean updateDish(UpdateDishDto updateDishDto);
    
    boolean deleteDish(String dishId);
}