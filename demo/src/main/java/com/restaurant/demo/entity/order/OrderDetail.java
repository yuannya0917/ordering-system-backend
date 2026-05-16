// OrderDetail.java
package com.restaurant.demo.entity.order;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("orderdetail")
public class OrderDetail {
    
    @TableField("orderID")
    private String orderId;
    
    @TableField("dishID")
    private String dishId;
    
    @TableField("dishNum")
    private Integer dishNum;
    
    @TableField("dishPrice")
    private Integer dishPrice;
}