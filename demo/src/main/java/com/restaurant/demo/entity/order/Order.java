// Order.java
package com.restaurant.demo.entity.order;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("orders")
public class Order {
    
    @TableId(type = IdType.ASSIGN_UUID)
    private String orderId;
    
    private String userId;
    
    private Integer orderPrice;
    
    private LocalDateTime orderTime;
    
    private String orderNote;
    
    private String orderStatus;
}