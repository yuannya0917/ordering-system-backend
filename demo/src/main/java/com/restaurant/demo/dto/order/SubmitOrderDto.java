package com.restaurant.demo.dto.order;

import lombok.Data;

@Data
public class SubmitOrderDto {
    private String userId;
    private String orderId;      // 添加手动传入的 orderId
    private String orderNote;
}