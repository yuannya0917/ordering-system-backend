package com.restaurant.demo.service.order;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.restaurant.demo.dto.order.AddToCartDto;
import com.restaurant.demo.dto.order.SubmitOrderDto;
import com.restaurant.demo.entity.order.Order;
import com.restaurant.demo.vo.order.CartVo;
import com.restaurant.demo.vo.order.OrderVo;
import com.restaurant.demo.vo.order.TotalAmountVo;  // 新增

public interface OrderService extends IService<Order> {
    
    void addToCart(AddToCartDto addToCartDto);
    
    void removeFromCart(String userId, String dishId);
    void notifyMerchantNewOrder(OrderVo orderVo);
    CartVo getCart(String userId);
    
    OrderVo submitOrder(SubmitOrderDto submitOrderDto);
    
    boolean updateOrderStatus(String orderId, String orderStatus);
    
    List<OrderVo> getHistoryOrders(String userId);
// 获取所有订单（支持筛选：用户ID、订单状态、时间范围）
    List<OrderVo> getAllOrders(String userId, String orderStatus, String startTime, String endTime);    TotalAmountVo getTotalAmount(String startTime, String endTime, String orderStatus);  // 新增
}