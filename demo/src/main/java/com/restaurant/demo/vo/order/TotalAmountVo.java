package com.restaurant.demo.vo.order;

import lombok.Data;

@Data
public class TotalAmountVo {
    private Integer totalAmount;   // 总金额（单位：元）
    private Integer orderCount;    // 订单数量
    private String startTime;      // 查询开始时间
    private String endTime;        // 查询结束时间
}