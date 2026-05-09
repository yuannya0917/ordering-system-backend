// OrderMapper.java
package com.restaurant.demo.mapper.order;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.restaurant.demo.entity.order.Order;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}