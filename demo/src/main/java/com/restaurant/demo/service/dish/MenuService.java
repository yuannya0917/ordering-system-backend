// MenuService.java
package com.restaurant.demo.service.dish;

import com.baomidou.mybatisplus.extension.service.IService;
import com.restaurant.demo.dto.dish.AddMenuDto;
import com.restaurant.demo.entity.dish.Menu;

public interface MenuService extends IService<Menu> {
    
    boolean addMenu(AddMenuDto addMenuDto);  // 参数类型是 AddMenuDto
    
    boolean deleteMenu(String menuId);
}