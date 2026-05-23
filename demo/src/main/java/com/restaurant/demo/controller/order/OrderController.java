package com.restaurant.demo.controller.order;  // 注意是 controller.order

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.restaurant.demo.dto.order.AddToCartDto;
import com.restaurant.demo.dto.order.RemoveFromCartDto;
import com.restaurant.demo.dto.order.SubmitOrderDto;
import com.restaurant.demo.dto.order.UpdateOrderStatusDto;
import com.restaurant.demo.service.order.OrderService;
import com.restaurant.demo.vo.ResultVo;
import com.restaurant.demo.vo.order.CartVo;
import com.restaurant.demo.vo.order.OrderVo;
import com.restaurant.demo.vo.order.TotalAmountVo;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;
    
    @PostMapping("/cart/add")
    public ResultVo<Void> addToCart(@RequestBody AddToCartDto addToCartDto) {
        orderService.addToCart(addToCartDto);
        return ResultVo.success("添加成功", null);
    }
    
    @DeleteMapping("/cart/remove")
    public ResultVo<Void> removeFromCart(@RequestBody RemoveFromCartDto removeFromCartDto) {
        orderService.removeFromCart(removeFromCartDto.getUserId(), removeFromCartDto.getDishId());
        return ResultVo.success("移除成功", null);
    }
    
    @GetMapping("/cart/{userId}")
    public ResultVo<CartVo> getCart(@PathVariable String userId) {
        CartVo cart = orderService.getCart(userId);
        return ResultVo.success(cart);
    }
    
    @PostMapping("/submit")
    public ResultVo<OrderVo> submitOrder(@RequestBody SubmitOrderDto submitOrderDto) {
        OrderVo order = orderService.submitOrder(submitOrderDto);
        return ResultVo.success("订单提交成功", order);
    }
    
    @GetMapping("/history/{userId}")
    public ResultVo<List<OrderVo>> getHistoryOrders(@PathVariable String userId) {
        List<OrderVo> orders = orderService.getHistoryOrders(userId);
        return ResultVo.success(orders);
    }
    @PutMapping("/status")
    public ResultVo<Boolean> updateOrderStatus(@RequestBody UpdateOrderStatusDto updateOrderStatusDto) {
    boolean result = orderService.updateOrderStatus(
        updateOrderStatusDto.getOrderId(), 
        updateOrderStatusDto.getOrderStatus()
    );
    return ResultVo.success(result);
    }
    // OrderController.java 新增
    @GetMapping("/totalAmount")
    public ResultVo<TotalAmountVo> getTotalAmount(
        @RequestParam(required = false) String startTime,
        @RequestParam(required = false) String endTime,
        @RequestParam(required = false) String orderStatus) {
    TotalAmountVo result = orderService.getTotalAmount(startTime, endTime, orderStatus);
    return ResultVo.success(result);
    }
    @GetMapping("/all")
    public ResultVo<List<OrderVo>> getAllOrders(
        @RequestParam(required = false) String userId,
        @RequestParam(required = false) String orderStatus){
    List<OrderVo> orders = orderService.getAllOrders(userId, orderStatus);
    return ResultVo.success(orders);
    }
}