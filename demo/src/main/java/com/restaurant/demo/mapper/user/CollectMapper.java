package com.restaurant.demo.mapper.user; // 包名是 mapper.user！

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.restaurant.demo.entity.user.Collect; // 导入 entity.user 下的Collect！
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CollectMapper extends BaseMapper<Collect> {}