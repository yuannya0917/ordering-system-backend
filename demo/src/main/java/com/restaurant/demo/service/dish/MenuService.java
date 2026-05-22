// MenuService.java
package com.restaurant.demo.service.dish;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.restaurant.demo.dto.dish.AddMenuDto;
import com.restaurant.demo.dto.dish.UpdateMenuDto;
import com.restaurant.demo.entity.dish.Menu;
import com.restaurant.demo.vo.dish.MenuVo;
public interface MenuService extends IService<Menu> {
    
    boolean addMenu(AddMenuDto addMenuDto);  // 参数类型是 AddMenuDto
    
    boolean deleteMenu(String menuId);
    boolean updateMenu(UpdateMenuDto updateMenuDto);
    List<com.restaurant.demo.vo.dish.MenuVo> getAllMenus();  // 使用完整类名
    List<MenuVo> getAllMenus(String menuName); 
    com.restaurant.demo.vo.dish.MenuVo getMenuById(String menuId);
    
}