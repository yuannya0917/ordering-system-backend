// OrderServiceImpl.java
package com.restaurant.demo.service.order.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.restaurant.demo.dto.order.AddToCartDto;
import com.restaurant.demo.dto.order.SubmitOrderDto;
import com.restaurant.demo.entity.order.Order;
import com.restaurant.demo.mapper.order.OrderMapper;
import com.restaurant.demo.service.order.OrderService;
import com.restaurant.demo.vo.order.CartItemVo;
import com.restaurant.demo.vo.order.CartVo;
import com.restaurant.demo.vo.order.OrderVo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    
    // 模拟购物车（实际项目中可用Redis或数据库）
    private Map<String, CartItemVo> cart = new HashMap<>();
    
    // 获取当前登录用户ID（实际从Session或Token获取）
    private String getCurrentUserId() {
        // TODO: 从登录上下文获取
        return "current_user_id";
    }
    
    @Override
    public void addToCart(AddToCartDto addToCartDto) {
        String dishId = addToCartDto.getDishId();
        
        CartItemVo existingItem = cart.get(dishId);
        if (existingItem != null) {
            existingItem.setDishNum(existingItem.getDishNum() + addToCartDto.getDishNum());
            existingItem.setTotalPrice(existingItem.getDishPrice() * existingItem.getDishNum());
        } else {
            CartItemVo newItem = new CartItemVo();
            newItem.setDishId(addToCartDto.getDishId());
            newItem.setDishName(addToCartDto.getDishName());
            newItem.setDishPrice(addToCartDto.getDishPrice());
            newItem.setDishNum(addToCartDto.getDishNum());
            newItem.setTotalPrice(addToCartDto.getDishPrice() * addToCartDto.getDishNum());
            cart.put(dishId, newItem);
        }
    }
    
    @Override
    public void removeFromCart(String dishId) {
        cart.remove(dishId);
    }
    
    @Override
    public CartVo getCart() {
        CartVo cartVo = new CartVo();
        List<CartItemVo> items = new ArrayList<>(cart.values());
        cartVo.setItems(items);
        
        int totalPrice = items.stream()
                .mapToInt(CartItemVo::getTotalPrice)
                .sum();
        cartVo.setTotalPrice(totalPrice);
        
        return cartVo;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderVo submitOrder(SubmitOrderDto submitOrderDto) {
        if (cart.isEmpty()) {
            throw new RuntimeException("购物车为空，无法提交订单");
        }
        
        String userId = getCurrentUserId();
        
        // 计算总价
        int totalPrice = cart.values().stream()
                .mapToInt(CartItemVo::getTotalPrice)
                .sum();
        
        // 创建订单
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderPrice(totalPrice);
        order.setOrderNote(submitOrderDto.getOrderNote());
        order.setOrderTime(LocalDateTime.now());
        order.setOrderStatus("待确认");
        
        this.save(order);
        
        // 清空购物车
        cart.clear();
        
        // 返回订单VO
        OrderVo orderVo = new OrderVo();
        orderVo.setOrderId(order.getOrderId());
        orderVo.setOrderPrice(order.getOrderPrice());
        orderVo.setOrderTime(order.getOrderTime());
        orderVo.setOrderNote(order.getOrderNote());
        orderVo.setOrderStatus(order.getOrderStatus());
        
        return orderVo;
    }
    
    @Override
    public List<OrderVo> getHistoryOrders() {
        String userId = getCurrentUserId();
        
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId)
               .orderByDesc(Order::getOrderTime);
        
        List<Order> orders = this.list(wrapper);
        
        return orders.stream().map(order -> {
            OrderVo vo = new OrderVo();
            vo.setOrderId(order.getOrderId());
            vo.setOrderPrice(order.getOrderPrice());
            vo.setOrderTime(order.getOrderTime());
            vo.setOrderNote(order.getOrderNote());
            vo.setOrderStatus(order.getOrderStatus());
            return vo;
        }).collect(Collectors.toList());
    }
}