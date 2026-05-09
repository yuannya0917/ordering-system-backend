// MenuMapper.java
package com.restaurant.demo.mapper.dish;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.restaurant.demo.entity.dish.Menu;

@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
}