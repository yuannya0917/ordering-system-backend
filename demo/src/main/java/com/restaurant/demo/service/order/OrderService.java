package com.restaurant.demo.service.order;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.restaurant.demo.dto.order.AddToCartDto;
import com.restaurant.demo.dto.order.SubmitOrderDto;
import com.restaurant.demo.entity.order.Order;
import com.restaurant.demo.vo.order.CartVo;
import com.restaurant.demo.vo.order.OrderVo;

public interface OrderService extends IService<Order> {
    
    void addToCart(AddToCartDto addToCartDto);
    
    void removeFromCart(String userId, String dishId);
    
    CartVo getCart(String userId);
    
    OrderVo submitOrder(SubmitOrderDto submitOrderDto);
    boolean updateOrderStatus(String orderId, String orderStatus);
    List<OrderVo> getHistoryOrders(String userId);
}