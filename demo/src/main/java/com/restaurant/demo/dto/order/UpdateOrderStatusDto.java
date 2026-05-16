// UpdateOrderStatusDto.java
package com.restaurant.demo.dto.order;

import lombok.Data;

@Data
public class UpdateOrderStatusDto {
    private String orderId;
    private String orderStatus;  // 如：待确认、已接单、制作中、已完成、已取消
}