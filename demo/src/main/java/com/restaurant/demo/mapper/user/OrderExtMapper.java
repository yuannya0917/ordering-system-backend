package com.restaurant.demo.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.restaurant.demo.entity.order.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface OrderExtMapper extends BaseMapper<Order> {
    
    @Select("SELECT SUM(orderPrice) FROM `order` WHERE userID = #{userId}")
    BigDecimal selectTotalAmountByUserId(String userId);
    
    @Select("SELECT COUNT(*) FROM `order` WHERE userID = #{userId}")
    Integer selectCountByUserId(String userId);
    
    // 新增：查询用户的订单ID列表
    @Select("SELECT orderID FROM `order` WHERE userID = #{userId} ORDER BY orderTime DESC")
    List<String> selectOrderIdsByUserId(String userId);
}