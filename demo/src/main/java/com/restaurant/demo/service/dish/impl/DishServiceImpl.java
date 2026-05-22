// DishServiceImpl.java
package com.restaurant.demo.service.dish.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.restaurant.demo.dto.dish.AddDishDto;
import com.restaurant.demo.dto.dish.UpdateDishDto;
import com.restaurant.demo.entity.dish.Dish;
import com.restaurant.demo.mapper.dish.DishMapper;
import com.restaurant.demo.service.dish.DishService;
import com.restaurant.demo.vo.dish.DishVo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    
    private final DishMapper dishMapper;
    
    @Override
    public DishVo getDish(String dishId) {
        Dish dish = this.getById(dishId);
        if (dish == null) {
            throw new RuntimeException("菜品不存在");
        }
        DishVo vo = new DishVo();
        BeanUtils.copyProperties(dish, vo);
        return vo;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addDish(AddDishDto addDishDto) {
        // 检查菜品名称是否已存在
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getDishName, addDishDto.getDishName());
        if (this.count(wrapper) > 0) {
            throw new RuntimeException("菜品名称已存在");
        }
        
        // 检查价格是否合法
        if (addDishDto.getDishPrice() == null || addDishDto.getDishPrice() < 0) {
            throw new RuntimeException("菜品价格不能为负数");
        }
        
        Dish dish = new Dish();
        BeanUtils.copyProperties(addDishDto, dish);
        return this.save(dish);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDish(UpdateDishDto updateDishDto) {
        // 检查菜品是否存在
        Dish existingDish = this.getById(updateDishDto.getDishId());
        if (existingDish == null) {
            throw new RuntimeException("菜品不存在");
        }
        
        // 检查菜品名称是否已被其他菜品使用
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getDishName, updateDishDto.getDishName())
               .ne(Dish::getDishId, updateDishDto.getDishId());
        if (this.count(wrapper) > 0) {
            throw new RuntimeException("菜品名称已存在");
        }
        
        // 检查价格是否合法
        if (updateDishDto.getDishPrice() == null || updateDishDto.getDishPrice() < 0) {
            throw new RuntimeException("菜品价格不能为负数");
        }
        
        BeanUtils.copyProperties(updateDishDto, existingDish);
        return this.updateById(existingDish);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDish(String dishId) {
        Dish dish = this.getById(dishId);
        if (dish == null) {
            throw new RuntimeException("菜品不存在");
        }
        return this.removeById(dishId);
    }
    @Override
    public List<DishVo> getDishList(String dishId, String dishName, String menuId) {
    List<Map<String, Object>> results = dishMapper.selectDishList(dishId, dishName, menuId);
    
    return results.stream().map(result -> {
        DishVo vo = new DishVo();
        vo.setDishImage((String) result.get("dishID"));
        vo.setDishName((String) result.get("dishName"));
        vo.setDishPrice((Integer) result.get("dishPrice"));
        vo.setDishIntroduction((String) result.get("dishIntroduction"));
        vo.setMenuName((String) result.get("menuName"));
        return vo;
    }).collect(Collectors.toList());
    }
}