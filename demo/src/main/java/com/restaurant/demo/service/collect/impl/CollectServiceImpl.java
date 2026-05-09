package com.restaurant.demo.service.collect.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.restaurant.demo.entity.user.Collect; // 导入 entity.user.Collect！
import com.restaurant.demo.mapper.user.CollectMapper; // 导入 mapper.user.CollectMapper！
import com.restaurant.demo.service.collect.CollectService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements CollectService {

    @Override
    public List<Collect> getCollectByUserId(String userId) {
        LambdaQueryWrapper<Collect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Collect::getUserId, userId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public boolean cancelCollect(String userId, String dishId) {
        LambdaQueryWrapper<Collect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Collect::getUserId, userId)
               .eq(Collect::getDishId, dishId);
        return baseMapper.delete(wrapper) > 0;
    }

    @Override
    public boolean isCollected(String userId, String dishId) {
        LambdaQueryWrapper<Collect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Collect::getUserId, userId)
               .eq(Collect::getDishId, dishId);
        return baseMapper.selectCount(wrapper) > 0;
    }
}