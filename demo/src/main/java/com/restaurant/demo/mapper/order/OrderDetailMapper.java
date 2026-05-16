// OrderDetailMapper.java
package com.restaurant.demo.mapper.order;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.restaurant.demo.entity.order.OrderDetail;

@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
    
    @Select("SELECT od.*, d.dishName FROM orderdetail od " +
            "LEFT JOIN dish d ON od.dishID = d.dishID " +
            "WHERE od.orderID = #{orderId}")
    List<Map<String, Object>> selectOrderDetailWithDishName(@Param("orderId") String orderId);
    
    @Select("SELECT SUM(dishNum * dishPrice) FROM orderdetail WHERE orderID = #{orderId}")
    Integer calculateTotalPriceByOrderId(@Param("orderId") String orderId);
}