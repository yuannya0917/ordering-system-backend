package com.restaurant.demo.service.order.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
import com.restaurant.demo.vo.order.TotalAmountVo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    
    // 购物车存储结构：userId -> Map<dishId, CartItemVo>
    private Map<String, Map<String, CartItemVo>> carts = new ConcurrentHashMap<>();
    
    @Override
    public void addToCart(AddToCartDto addToCartDto) {
        String userId = addToCartDto.getUserId();
        String dishId = addToCartDto.getDishId();
        
        // 获取或创建用户的购物车
        Map<String, CartItemVo> userCart = carts.computeIfAbsent(userId, k -> new ConcurrentHashMap<>());
        
        CartItemVo existingItem = userCart.get(dishId);
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
            userCart.put(dishId, newItem);
        }
    }
    
    @Override
    public void removeFromCart(String userId, String dishId) {
        Map<String, CartItemVo> userCart = carts.get(userId);
        if (userCart != null) {
            userCart.remove(dishId);
        }
    }
    
    @Override
    public CartVo getCart(String userId) {
        CartVo cartVo = new CartVo();
        Map<String, CartItemVo> userCart = carts.getOrDefault(userId, new ConcurrentHashMap<>());
        List<CartItemVo> items = new ArrayList<>(userCart.values());
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
    String userId = submitOrderDto.getUserId();
    String orderId = submitOrderDto.getOrderId();  // 获取手动传入的 orderId
    
    if (orderId == null || orderId.trim().isEmpty()) {
        throw new RuntimeException("订单ID不能为空");
    }
    
    Map<String, CartItemVo> userCart = carts.get(userId);
    
    if (userCart == null || userCart.isEmpty()) {
        throw new RuntimeException("购物车为空，无法提交订单");
    }
    
    // 计算总价
    int totalPrice = userCart.values().stream()
            .mapToInt(CartItemVo::getTotalPrice)
            .sum();
    
    // 创建订单
    Order order = new Order();
    order.setOrderId(orderId);  // 使用手动传入的 orderId
    order.setUserId(userId);
    order.setOrderPrice(totalPrice);
    order.setOrderNote(submitOrderDto.getOrderNote());
    order.setOrderTime(LocalDateTime.now());
    order.setOrderStatus("0");
    
    this.save(order);
    
    // 清空用户购物车
    carts.remove(userId);
    
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
    public List<OrderVo> getHistoryOrders(String userId) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId)
               .orderByDesc(Order::getOrderTime);
        
        List<Order> orders = this.list(wrapper);
        
        return orders.stream().map(order -> {
            OrderVo vo = new OrderVo();
            vo.setOrderId(order.getOrderId());
            vo.setUserId(order.getUserId());
            vo.setOrderPrice(order.getOrderPrice());
            vo.setOrderTime(order.getOrderTime());
            vo.setOrderNote(order.getOrderNote());
            vo.setOrderStatus(order.getOrderStatus());
            return vo;
        }).collect(Collectors.toList());
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrderStatus(String orderId, String orderStatus) {
    Order order = this.getById(orderId);
    if (order == null) {
        throw new RuntimeException("订单不存在");
    }
    order.setOrderStatus(orderStatus);
    return this.updateById(order);
}
     @Override
    public TotalAmountVo getTotalAmount(String startTime, String endTime, String orderStatus) {
    LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
    
    // 时间范围过滤
    if (startTime != null && !startTime.isEmpty()) {
        wrapper.ge(Order::getOrderTime, LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
    if (endTime != null && !endTime.isEmpty()) {
        wrapper.le(Order::getOrderTime, LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
    
    // 订单状态过滤
    if (orderStatus != null && !orderStatus.isEmpty()) {
        wrapper.eq(Order::getOrderStatus, orderStatus);
    } else {
        // 默认只统计已完成的订单
        wrapper.eq(Order::getOrderStatus, "3");
    }
    
    List<Order> orders = this.list(wrapper);
    
    TotalAmountVo vo = new TotalAmountVo();
    vo.setOrderCount(orders.size());
    vo.setTotalAmount(orders.stream().mapToInt(Order::getOrderPrice).sum());
    vo.setStartTime(startTime);
    vo.setEndTime(endTime);
    
    return vo;
}
}