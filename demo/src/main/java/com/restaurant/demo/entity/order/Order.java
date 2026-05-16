package com.restaurant.demo.entity.order;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("`order`")
public class Order {
    
    @TableId(type = IdType.INPUT)
    @TableField("orderID")
    private String orderId;
    
    @TableField("userID")
    private String userId;
    
    @TableField("orderPrice")
    private Integer orderPrice;
    
    @TableField("orderTime")
    private LocalDateTime orderTime;
    
    @TableField("orderNote")
    private String orderNote;
    
    @TableField("orderStatus")
    private String orderStatus;
}