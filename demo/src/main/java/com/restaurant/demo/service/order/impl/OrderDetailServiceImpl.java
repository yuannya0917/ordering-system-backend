// OrderDetailServiceImpl.java
package com.restaurant.demo.service.order.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.restaurant.demo.dto.order.AddOrderDetailDto;
import com.restaurant.demo.dto.order.DeleteOrderDetailDto;
import com.restaurant.demo.entity.order.OrderDetail;
import com.restaurant.demo.mapper.order.OrderDetailMapper;
import com.restaurant.demo.service.order.OrderDetailService;
import com.restaurant.demo.vo.order.OrderDetailVo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
    
    private final OrderDetailMapper orderDetailMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addOrderDetail(AddOrderDetailDto addOrderDetailDto) {
        // 检查是否已存在相同的订单详情
        LambdaQueryWrapper<OrderDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDetail::getOrderId, addOrderDetailDto.getOrderId())
               .eq(OrderDetail::getDishId, addOrderDetailDto.getDishId());
        if (this.count(wrapper) > 0) {
            throw new RuntimeException("订单中已存在该菜品");
        }
        
        // 检查数量是否合法
        if (addOrderDetailDto.getDishNum() == null || addOrderDetailDto.getDishNum() <= 0) {
            throw new RuntimeException("菜品数量必须大于0");
        }
        
        OrderDetail orderDetail = new OrderDetail();
        BeanUtils.copyProperties(addOrderDetailDto, orderDetail);
        return this.save(orderDetail);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteOrderDetail(DeleteOrderDetailDto deleteOrderDetailDto) {
        LambdaQueryWrapper<OrderDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDetail::getOrderId, deleteOrderDetailDto.getOrderId())
               .eq(OrderDetail::getDishId, deleteOrderDetailDto.getDishId());
        
        OrderDetail orderDetail = this.getOne(wrapper);
        if (orderDetail == null) {
            throw new RuntimeException("订单详情不存在");
        }
        
        return this.remove(wrapper);
    }
    
    @Override
    public List<OrderDetailVo> getOrderDetailsByOrderId(String orderId) {
        List<Map<String, Object>> results = orderDetailMapper.selectOrderDetailWithDishName(orderId);
        
        return results.stream().map(result -> {
            OrderDetailVo vo = new OrderDetailVo();
            vo.setOrderId((String) result.get("orderID"));
            vo.setDishId((String) result.get("dishID"));
            vo.setDishName((String) result.get("dishName"));
            vo.setDishNum((Integer) result.get("dishNum"));
            vo.setDishPrice((Integer) result.get("dishPrice"));
            vo.setTotalPrice((Integer) result.get("dishNum") * (Integer) result.get("dishPrice"));
            return vo;
        }).collect(Collectors.toList());
    }
    
    @Override
    public Integer getTotalPriceByOrderId(String orderId) {
        Integer totalPrice = orderDetailMapper.calculateTotalPriceByOrderId(orderId);
        return totalPrice != null ? totalPrice : 0;
    }
}