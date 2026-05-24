package com.restaurant.demo.service.order.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    
    private final OrderMapper orderMapper;
    private final SimpMessagingTemplate messagingTemplate;
    
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
    
    // 获取用户购物车
    Map<String, CartItemVo> userCart = carts.get(userId);
    
    if (userCart == null || userCart.isEmpty()) {
        throw new RuntimeException("购物车为空，无法提交订单");
    }
    
    // 计算总价
    int totalPrice = userCart.values().stream()
            .mapToInt(CartItemVo::getTotalPrice)
            .sum();
    
    // 自动生成订单ID
    String orderId = generateOrderId();
    
    // 创建订单
    Order order = new Order();
    order.setOrderId(orderId);
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
    orderVo.setUserId(order.getUserId());
    orderVo.setOrderPrice(order.getOrderPrice());
    orderVo.setOrderTime(order.getOrderTime());
    orderVo.setOrderNote(order.getOrderNote());
    orderVo.setOrderStatus(order.getOrderStatus());
    
    // 推送新订单通知
    notifyMerchantNewOrder(orderVo);
    
    return orderVo;
    }

/**
 * 生成订单ID
 * 格式：yyMMdd + 5位随机数（共11位）
 * 例如：26052412345
 */
private synchronized String generateOrderId() {
    String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
    String orderId;
    do {
        int random = (int) (Math.random() * 90000) + 10000; // 10000-99999
        orderId = date + random;
    } while (checkOrderIdExists(orderId));
    return orderId;
}

/**
 * 检查订单ID是否已存在
 */
private boolean checkOrderIdExists(String orderId) {
    return this.getById(orderId) != null;
}
    
    @Override
    public void notifyMerchantNewOrder(OrderVo orderVo) {
        // 推送到商家订阅的频道
        messagingTemplate.convertAndSend("/topic/merchant/new-orders", orderVo);
        System.out.println("新订单已推送: " + orderVo.getOrderId());
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
    
    String oldStatus = order.getOrderStatus();
    order.setOrderStatus(orderStatus);
    boolean result = this.updateById(order);
    
    // 状态变更后，推送消息给用户
    if (result) {
        notifyUserOrderStatusChanged(order, oldStatus, orderStatus);
    }
    
    return result;
}

/**
 * 通知用户订单状态变更
 */
private void notifyUserOrderStatusChanged(Order order, String oldStatus, String newStatus) {
    // 构建推送消息
    Map<String, Object> message = new HashMap<>();
    message.put("orderId", order.getOrderId());
    message.put("oldStatus", oldStatus);
    message.put("newStatus", newStatus);
    message.put("message", getStatusMessage(newStatus));
    
    // 推送到用户专属频道
    String destination = "/topic/user/" + order.getUserId() + "/order-status";
    messagingTemplate.convertAndSend(destination, message);
    
    System.out.println("订单状态变更已推送: 用户=" + order.getUserId() + 
                       ", 订单=" + order.getOrderId() + 
                       ", 状态=" + oldStatus + "->" + newStatus);
}

/**
 * 根据状态码获取状态描述
 */
private String getStatusMessage(String status) {
    switch (status) {
        case "0": return "订单已提交，等待商家确认";
        case "1": return "商家已接单，正在准备";
        case "2": return "订单已完成，请用餐";
        default: return "订单状态已更新";
    }
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
            // 默认统计状态为 '2'（制作中）的订单
            wrapper.eq(Order::getOrderStatus, "2");
        }
        
        List<Order> orders = this.list(wrapper);
        
        TotalAmountVo vo = new TotalAmountVo();
        vo.setOrderCount(orders.size());
        vo.setTotalAmount(orders.stream().mapToInt(Order::getOrderPrice).sum());
        vo.setStartTime(startTime);
        vo.setEndTime(endTime);
        
        return vo;
    }
    @Override
public List<OrderVo> getAllOrders(String userId, String orderStatus, String startTime, String endTime) {
    LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
    
    // 按用户ID筛选
    if (userId != null && !userId.trim().isEmpty()) {
        wrapper.eq(Order::getUserId, userId);
    }
    
    // 按订单状态筛选
    if (orderStatus != null && !orderStatus.trim().isEmpty()) {
        wrapper.eq(Order::getOrderStatus, orderStatus);
    }
    
    // 按开始时间筛选
    if (startTime != null && !startTime.trim().isEmpty()) {
        wrapper.ge(Order::getOrderTime, LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
    
    // 按结束时间筛选
    if (endTime != null && !endTime.trim().isEmpty()) {
        wrapper.le(Order::getOrderTime, LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
    
    // 按下单时间倒序
    wrapper.orderByDesc(Order::getOrderTime);
    
    List<Order> orders = this.list(wrapper);
    
    return orders.stream().map(this::convertToOrderVo).collect(Collectors.toList());
}

private OrderVo convertToOrderVo(Order order) {
    OrderVo vo = new OrderVo();
    vo.setOrderId(order.getOrderId());
    vo.setUserId(order.getUserId());
    vo.setOrderPrice(order.getOrderPrice());
    vo.setOrderTime(order.getOrderTime());
    vo.setOrderNote(order.getOrderNote());
    vo.setOrderStatus(order.getOrderStatus());
    return vo;
}
}