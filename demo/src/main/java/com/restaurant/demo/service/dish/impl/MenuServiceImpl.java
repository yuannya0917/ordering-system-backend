// MenuServiceImpl.java
package com.restaurant.demo.service.dish.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.restaurant.demo.dto.dish.AddMenuDto;
import com.restaurant.demo.dto.dish.UpdateMenuDto;
import com.restaurant.demo.entity.dish.Menu;
import com.restaurant.demo.mapper.dish.MenuMapper;
import com.restaurant.demo.service.dish.MenuService;
import com.restaurant.demo.vo.dish.MenuVo;

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
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateMenu(UpdateMenuDto updateMenuDto) {
    Menu menu = this.getById(updateMenuDto.getMenuId());
    if (menu == null) {
        throw new RuntimeException("菜单不存在");
    }
    
    LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(Menu::getMenuName, updateMenuDto.getMenuName())
           .ne(Menu::getMenuId, updateMenuDto.getMenuId());
    if (this.count(wrapper) > 0) {
        throw new RuntimeException("菜单名称已存在");
    }
    
    BeanUtils.copyProperties(updateMenuDto, menu);
    return this.updateById(menu);
}
@Override
public List<MenuVo> getAllMenus() {
    List<Menu> menus = this.list();
    return menus.stream().map(menu -> {
        MenuVo vo = new MenuVo();
        BeanUtils.copyProperties(menu, vo);
        return vo;
    }).collect(Collectors.toList());
}

    @Override
    public MenuVo getMenuById(String menuId) {
    Menu menu = this.getById(menuId);
    if (menu == null) {
        throw new RuntimeException("菜单不存在");
    }
    MenuVo vo = new MenuVo();
    BeanUtils.copyProperties(menu, vo);
    return vo;
}
}