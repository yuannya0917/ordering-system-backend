// DishController.java
package com.restaurant.demo.controller.dish;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.restaurant.demo.dto.dish.AddDishDto;
import com.restaurant.demo.dto.dish.DeleteDishDto;
import com.restaurant.demo.dto.dish.UpdateDishDto;
import com.restaurant.demo.service.dish.DishService;
import com.restaurant.demo.vo.ResultVo;
import com.restaurant.demo.vo.dish.DishVo;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dish")
@RequiredArgsConstructor
public class DishController {
    
    private final DishService dishService;
    
    @GetMapping("/get/{dishId}")
    public ResultVo<DishVo> getDish(@PathVariable String dishId) {
        DishVo dishVo = dishService.getDish(dishId);
        return ResultVo.success(dishVo);
    }
    
    @PostMapping("/add")
    public ResultVo<Boolean> addDish(@RequestBody AddDishDto addDishDto) {
        boolean result = dishService.addDish(addDishDto);
        return ResultVo.success(result);
    }
    
    @PutMapping("/update")
    public ResultVo<Boolean> updateDish(@RequestBody UpdateDishDto updateDishDto) {
        boolean result = dishService.updateDish(updateDishDto);
        return ResultVo.success(result);
    }
    
    @DeleteMapping("/delete")
    public ResultVo<Boolean> deleteDish(@RequestBody DeleteDishDto deleteDishDto) {
        boolean result = dishService.deleteDish(deleteDishDto.getDishId());
        return ResultVo.success(result);
    }
    @GetMapping("/list")
    public ResultVo<List<DishVo>> getDishList(
        @RequestParam(required = false) String dishId,
        @RequestParam(required = false) String dishName,
        @RequestParam(required = false) String menuId) {
    List<DishVo> list = dishService.getDishList(dishId, dishName, menuId);
    return ResultVo.success(list);
    }
}