// OrderDetailController.java
package com.restaurant.demo.controller.order;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restaurant.demo.dto.order.AddOrderDetailDto;
import com.restaurant.demo.dto.order.DeleteOrderDetailDto;
import com.restaurant.demo.service.order.OrderDetailService;
import com.restaurant.demo.vo.ResultVo;
import com.restaurant.demo.vo.order.OrderDetailVo;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orderdetail")
@RequiredArgsConstructor
public class OrderDetailController {
    
    private final OrderDetailService orderDetailService;
    
    @PostMapping("/add")
    public ResultVo<Boolean> addOrderDetail(@RequestBody AddOrderDetailDto addOrderDetailDto) {
        boolean result = orderDetailService.addOrderDetail(addOrderDetailDto);
        return ResultVo.success(result);
    }
    
    @DeleteMapping("/delete")
    public ResultVo<Boolean> deleteOrderDetail(@RequestBody DeleteOrderDetailDto deleteOrderDetailDto) {
        boolean result = orderDetailService.deleteOrderDetail(deleteOrderDetailDto);
        return ResultVo.success(result);
    }
    
    @GetMapping("/list/{orderId}")
    public ResultVo<List<OrderDetailVo>> getOrderDetails(@PathVariable String orderId) {
        List<OrderDetailVo> details = orderDetailService.getOrderDetailsByOrderId(orderId);
        return ResultVo.success(details);
    }
    
    @GetMapping("/total/{orderId}")
    public ResultVo<Integer> getTotalPrice(@PathVariable String orderId) {
        Integer totalPrice = orderDetailService.getTotalPriceByOrderId(orderId);
        return ResultVo.success(totalPrice);
    }
}