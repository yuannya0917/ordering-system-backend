package com.restaurant.demo.dto.order;

import lombok.Data;

@Data
public class SubmitOrderDto {
    private String userId;
    private String orderNote;
}