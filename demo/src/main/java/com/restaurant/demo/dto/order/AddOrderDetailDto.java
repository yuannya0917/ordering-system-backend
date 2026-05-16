// AddOrderDetailDto.java
package com.restaurant.demo.dto.order;

import lombok.Data;

@Data
public class AddOrderDetailDto {
    private String orderId;
    private String dishId;
    private Integer dishNum;
    private Integer dishPrice;
}