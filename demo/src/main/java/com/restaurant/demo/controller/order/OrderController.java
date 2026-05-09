// OrderController.java
package com.restaurant.demo.controller.order;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restaurant.demo.dto.order.AddToCartDto;
import com.restaurant.demo.dto.order.RemoveFromCartDto;
import com.restaurant.demo.dto.order.SubmitOrderDto;
import com.restaurant.demo.service.order.OrderService;
import com.restaurant.demo.vo.ResultVo;
import com.restaurant.demo.vo.order.CartVo;
import com.restaurant.demo.vo.order.OrderVo;

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
        orderService.removeFromCart(removeFromCartDto.getDishId());
        return ResultVo.success("移除成功", null);
    }
    
    @GetMapping("/cart")
    public ResultVo<CartVo> getCart() {
        CartVo cart = orderService.getCart();
        return ResultVo.success(cart);
    }
    
    @PostMapping("/submit")
    public ResultVo<OrderVo> submitOrder(@RequestBody SubmitOrderDto submitOrderDto) {
        OrderVo order = orderService.submitOrder(submitOrderDto);
        return ResultVo.success("订单提交成功", order);
    }
    
    @GetMapping("/history")
    public ResultVo<List<OrderVo>> getHistoryOrders() {
        List<OrderVo> orders = orderService.getHistoryOrders();
        return ResultVo.success(orders);
    }
}