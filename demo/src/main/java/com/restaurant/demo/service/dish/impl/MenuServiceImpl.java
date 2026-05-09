// MenuServiceImpl.java
package com.restaurant.demo.service.dish.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.restaurant.demo.dto.dish.AddMenuDto;
import com.restaurant.demo.entity.dish.Menu;
import com.restaurant.demo.mapper.dish.MenuMapper;
import com.restaurant.demo.service.dish.MenuService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addMenu(AddMenuDto addMenuDto) {  // 参数类型必须与接口一致
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getMenuName, addMenuDto.getMenuName());
        if (this.count(wrapper) > 0) {
            throw new RuntimeException("菜单名称已存在");
        }
        
        Menu menu = new Menu();
        BeanUtils.copyProperties(addMenuDto, menu);
        return this.save(menu);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteMenu(String menuId) {
        Menu menu = this.getById(menuId);
        if (menu == null) {
            throw new RuntimeException("菜单不存在");
        }
        return this.removeById(menuId);
    }
}