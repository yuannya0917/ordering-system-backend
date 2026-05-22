// MenuController.java
package com.restaurant.demo.controller.dish;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;  // 改用 ResultVo
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.restaurant.demo.dto.dish.AddMenuDto;
import com.restaurant.demo.dto.dish.UpdateMenuDto;
import com.restaurant.demo.dto.dish.deleteMenuDto;
import com.restaurant.demo.service.dish.MenuService;
import com.restaurant.demo.vo.ResultVo;
import com.restaurant.demo.vo.dish.MenuVo;

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

    @PutMapping("/update")
    public ResultVo<Boolean> updateMenu(@RequestBody UpdateMenuDto updateMenuDto) {
    boolean result = menuService.updateMenu(updateMenuDto);
    return ResultVo.success(result);
    }
    @GetMapping("/list")
    public ResultVo<List<MenuVo>> getAllMenus(
        @RequestParam(required = false) String menuName) {
    List<MenuVo> menus = menuService.getAllMenus(menuName);
    return ResultVo.success(menus);
    }

    @GetMapping("/get/{menuId}")
    public ResultVo<MenuVo> getMenuById(@PathVariable String menuId) {
    MenuVo menu = menuService.getMenuById(menuId);
    return ResultVo.success(menu);
    }
}