// DeleteOrderDetailDto.java
package com.restaurant.demo.dto.order;

import lombok.Data;

@Data
public class DeleteOrderDetailDto {
    private String orderId;
    private String dishId;
}