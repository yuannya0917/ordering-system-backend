// MenuController.java
package com.restaurant.demo.controller.dish;

import org.springframework.web.bind.annotation.DeleteMapping;  // 改用 ResultVo
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restaurant.demo.dto.dish.AddMenuDto;
import com.restaurant.demo.dto.dish.deleteMenuDto;
import com.restaurant.demo.service.dish.MenuService;
import com.restaurant.demo.vo.ResultVo;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {
    
    private final MenuService menuService;
    
    @PostMapping("/add")
    public ResultVo<Boolean> addMenu(@RequestBody AddMenuDto addMenuDto) {
        boolean result = menuService.addMenu(addMenuDto);
        return ResultVo.success(result);
    }
    
    @DeleteMapping("/delete")
    public ResultVo<Boolean> deleteMenu(@RequestBody deleteMenuDto deleteMenuDto) {
        boolean result = menuService.deleteMenu(deleteMenuDto.getMenuId());
        return ResultVo.success(result);
    }
}