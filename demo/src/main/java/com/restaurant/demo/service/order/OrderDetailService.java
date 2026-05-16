// OrderDetailService.java
package com.restaurant.demo.service.order;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.restaurant.demo.dto.order.AddOrderDetailDto;
import com.restaurant.demo.dto.order.DeleteOrderDetailDto;
import com.restaurant.demo.entity.order.OrderDetail;
import com.restaurant.demo.vo.order.OrderDetailVo;

public interface OrderDetailService extends IService<OrderDetail> {
    
    boolean addOrderDetail(AddOrderDetailDto addOrderDetailDto);
    
    boolean deleteOrderDetail(DeleteOrderDetailDto deleteOrderDetailDto);
    
    List<OrderDetailVo> getOrderDetailsByOrderId(String orderId);
    
    Integer getTotalPriceByOrderId(String orderId);
}