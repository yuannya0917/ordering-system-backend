package com.restaurant.demo.mapper.collect;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.restaurant.demo.entity.collect.Collect;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CollectMapper extends BaseMapper<Collect> {
    // 无额外方法，使用 MyBatis-Plus 默认
}